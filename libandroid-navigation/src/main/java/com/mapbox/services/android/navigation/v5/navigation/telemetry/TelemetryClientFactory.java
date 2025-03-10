package com.mapbox.services.android.navigation.v5.navigation.telemetry;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

class TelemetryClientFactory {
  private static final String LOG_TAG = "TelemetryClientFactory";
  private static final String RETRIEVING_APP_META_DATA_ERROR_MESSAGE = "Failed when retrieving app meta-data: %s";
  private final String accessToken;
  private final String userAgent;
  private final Logger logger;
  private final CertificateBlacklist certificateBlacklist;

  TelemetryClientFactory(String accessToken, String userAgent, Logger logger,
                         CertificateBlacklist certificateBlacklist) {
    this.accessToken = accessToken;
    this.userAgent = userAgent;
    this.logger = logger;
    this.certificateBlacklist = certificateBlacklist;
  }

  TelemetryClient obtainTelemetryClient(Context context) {
    try {
      ApplicationInfo appInformation = context.getPackageManager().getApplicationInfo(context.getPackageName(),
        PackageManager.GET_META_DATA);
      if (appInformation != null && appInformation.metaData != null) {
        EnvironmentChain environmentChain = new EnvironmentChain();
        return buildClientFrom(environmentChain.setup().obtainServerInformation(appInformation.metaData), context);
      }
    } catch (Exception exception) {
      logger.error(LOG_TAG, String.format(RETRIEVING_APP_META_DATA_ERROR_MESSAGE, exception.getMessage()));
    }
    return buildTelemetryClient(Environment.COM, certificateBlacklist, context);
  }

  TelemetryClient obtainTelemetryClient(Environment environment, Context context) {
    if (environment == Environment.CHINA) {
      return buildTelemetryClient(environment, certificateBlacklist, context);
    } else {
      try {
        ApplicationInfo appInformation = context.getPackageManager().getApplicationInfo(context.getPackageName(),
          PackageManager.GET_META_DATA);
        if (appInformation != null && appInformation.metaData != null) {
          return buildClientFrom(new ComServerInformation().obtainServerInformation(appInformation.metaData), context);
        }
      } catch (Exception exception) {
        logger.error(LOG_TAG, String.format(RETRIEVING_APP_META_DATA_ERROR_MESSAGE, exception.getMessage()));
      }
    }

    return buildTelemetryClient(Environment.COM, certificateBlacklist, context);
  }

  private TelemetryClient buildTelemetryClient(Environment environment,
                                               CertificateBlacklist certificateBlacklist,
                                               Context context) {
    return new TelemetryClient(accessToken,
      userAgent,
      TelemetryUtils.createReformedFullUserAgent(context),
      new TelemetryClientSettings.Builder(context).environment(environment).build(),
      logger,
      certificateBlacklist,
      environment == Environment.CHINA);
  }

  private TelemetryClient buildTelemetryClientCustom(ServerInformation serverInformation,
                                                     CertificateBlacklist certificateBlacklist,
                                                     Context context) {
    TelemetryClientSettings telemetryClientSettings = new TelemetryClientSettings.Builder(context)
      .environment(serverInformation.getEnvironment())
      .baseUrl(TelemetryClientSettings.configureUrlHostname(serverInformation.getHostname()))
      .build();
    String serverToken = serverInformation.getAccessToken();
    return new TelemetryClient(serverToken != null ? serverToken : accessToken,
      userAgent,
      TelemetryUtils.createReformedFullUserAgent(context),
      telemetryClientSettings,
      logger,
      certificateBlacklist,
      serverInformation.getEnvironment() == Environment.CHINA);
  }

  private TelemetryClient buildClientFrom(ServerInformation serverInformation, Context context) {
    Environment environment = serverInformation.getEnvironment();
    switch (environment) {
      case STAGING:
        return buildTelemetryClientCustom(serverInformation, certificateBlacklist, context);
      default:
        if (!TelemetryUtils.isEmpty(serverInformation.getHostname())) {
          return buildTelemetryClientCustom(serverInformation, certificateBlacklist, context);
        } else {
          return buildTelemetryClient(environment, certificateBlacklist, context);
        }
    }
  }
}
