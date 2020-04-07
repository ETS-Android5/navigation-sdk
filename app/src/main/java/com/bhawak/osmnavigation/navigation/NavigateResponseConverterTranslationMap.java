package com.bhawak.osmnavigation.navigation;

import android.util.Log;

import com.graphhopper.util.TranslationMap;

import java.io.File;
import java.util.Arrays;

import static com.graphhopper.util.Helper.getLocale;

/**
 * Simple class that extends the TranslationMap to add new translations.
 *
 * @author Robin Boldt
 */
public class NavigateResponseConverterTranslationMap extends TranslationMap {

    public NavigateResponseConverterTranslationMap() {
        super();
    }

    /**
     * This loads the translation files from the specified folder.
     */
    @Override
    public TranslationMap doImport() {

        try {
            for (String locale : Arrays.asList("en_US", "en_US")) {
                TranslationHashMap trMap = new TranslationHashMap(getLocale(locale));
//                trMap.doImport(NavigateResponseConverterTranslationMap.class.getResourceAsStream(locale + ".txt"));
                add(trMap);
            }
            //Not accessible, how bad is this?
            //postImportHook();
            return this;
        } catch (Exception ex) {
//            Log.e("error", ex.getMessage());
            throw new RuntimeException(ex);
        }
//        return  null;
    }

}
