/*
 * Copyright 2011 Sergey Skladchikov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package sk.benko.appsresource.client.util;

import com.google.gwt.core.client.GWT;

/**
 * This sigleton is used to centrilize and simplify theme management.
 *
 * @author <a href="mailto:sskladchikov@gmail.com">Sergey Skladchikov</a>
 * @since 1.0.0
 */                                                
public class ThemeHelper {
    /** an instance of this class */
    private static final ThemeHelper instance = new ThemeHelper();
    /** base directory name (a subfolder to store themes) that can include a context name if starts with '/' */
    private String baseDirectory;

    /**
     * Creates an instance of this class.
     */
    private ThemeHelper() {}

    /**
     * This method returns an instance of this class.
     *
     * @return an instance.
     */
    public static ThemeHelper getInstance() {
        return instance;
    }

    /**
     * This method gets a full name of the specified image using the theme name.
     *
     * @param shortName is a short name of the image.
     * @return is a full name.
     */
    public String getFullImageName(String shortName) {
        return getBaseDirectory() + "../images/" + shortName;
    }

    /**
     * Gets a full resource name applying the {@link #baseDirectory} as a root for the
     * specified relative path.
     *
     * @param relativePath is a path relative the base directory.
     * @return a full source name.
     */
    public String getFullResourceName(String relativePath) {
        return getBaseDirectory() + relativePath;
    }

    /**
     * Setter for property 'baseDirectory'.
     *
     * @param baseDirectory Value to set for property 'baseDirectory'.
     */
    public void setBaseDirectory(String baseDirectory) {
        this.baseDirectory = baseDirectory;
    }

    /**
     * This method returns a base directory that ends with '/' if it's specified and
     * <code>GWT.getModuleBaseURL()</code> by default.
     *
     * @return a base directory name (never <code>null</code>).
     */
    protected String getBaseDirectory() {
        if (baseDirectory != null && baseDirectory.length() > 0)
            return baseDirectory + "/";
        else
            return GWT.getModuleBaseURL();
    }
}
