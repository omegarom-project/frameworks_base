/*
 * Copyright (C) 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.internal.statusbar;

import android.content.om.IOverlayManager;
import android.content.om.OverlayInfo;
import android.os.RemoteException;

public class ThemeAccentUtils {

    public static final String TAG = "ThemeAccentUtils";

    // Stock dark theme package
    private static final String STOCK_DARK_THEME = "com.android.systemui.theme.dark";

    // Notification themes
    private static final String NOTIFICATION_DARK_THEME = "com.android.system.notification.dark";
    private static final String NOTIFICATION_BLACK_THEME = "com.android.system.notification.black";

    // Dark themes
    private static final String[] DARK_THEMES = {
        "com.android.system.theme.dark",
        "com.android.settings.theme.dark",
        "com.android.settings.intelligence.theme.dark",
        "com.android.sysui.theme.dark",
        "com.android.gboard.theme.dark",
    };

    // Black themes
    private static final String[] BLACK_THEMES = {
        "com.android.system.theme.black",
        "com.android.settings.theme.black",
        "com.android.settings.intelligence.theme.black",
        "com.android.sysui.theme.black",
        "com.android.gboard.theme.black",
    };

    // Accents
    private static final String[] ACCENTS = {
        "default_accent", // 0
        "com.accents.red", // 1
        "com.accents.pink", // 2
        "com.accents.purple", // 3
        "com.accents.deeppurple", // 4
        "com.accents.indigo", // 5
        "com.accents.blue", // 6
        "com.accents.lightblue", // 7
        "com.accents.cyan", // 8
        "com.accents.teal", // 9
        "com.accents.green", // 10
        "com.accents.lightgreen", // 11
        "com.accents.lime", // 12
        "com.accents.yellow", // 13
        "com.accents.amber", // 14
        "com.accents.orange", // 15
        "com.accents.deeporange", // 16
        "com.accents.brown", // 17
        "com.accents.grey", // 18
        "com.accents.bluegrey", // 19
        "com.accents.black", // 20
        "com.accents.white", // 21
        "com.accents.userone", // 22
        "com.accents.usertwo", // 23
        "com.accents.userthree", // 24
        "com.accents.userfour", // 25
        "com.accents.userfive", // 26
        "com.accents.usersix", // 27
        "com.accents.userseven", // 28
    };

    // Unloads the stock dark theme
    private static void unloadStockDarkTheme(IOverlayManager om, int userId) {
        OverlayInfo themeInfo = null;
        try {
            themeInfo = om.getOverlayInfo(STOCK_DARK_THEME,
                    userId);
            if (themeInfo != null && themeInfo.isEnabled()) {
                om.setEnabled(STOCK_DARK_THEME,
                        false /*disable*/, userId);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    // Unloads dark notification theme
    private static void unloadDarkNotificationTheme(IOverlayManager om, int userId) {
        try {
            om.setEnabled(NOTIFICATION_DARK_THEME, false, userId);
        } catch (RemoteException e) {
        }
    }

    // Unloads black notification theme
    private static void unloadBlackNotificationTheme(IOverlayManager om, int userId) {
        try {
            om.setEnabled(NOTIFICATION_BLACK_THEME, false, userId);
        } catch (RemoteException e) {
        }
    }

    // Unloads the dark themes
    private static void unloadDarkTheme(IOverlayManager om, int userId) {
        for (String theme : DARK_THEMES) {
            try {
                om.setEnabled(theme, false, userId);
            } catch (RemoteException e) {
            }
        }
    }

    // Unloads the black themes
    private static void unloadBlackTheme(IOverlayManager om, int userId) {
        for (String theme : BLACK_THEMES) {
            try {
                om.setEnabled(theme, false, userId);
            } catch (RemoteException e) {
            }
        }
    }

    // Check for the dark system theme
    public static boolean isUsingDarkTheme(IOverlayManager om, int userId) {
        OverlayInfo themeInfo = null;
        try {
            themeInfo = om.getOverlayInfo(DARK_THEMES[0],
                    userId);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return themeInfo != null && themeInfo.isEnabled();
    }

    // Check for the black system theme
    public static boolean isUsingBlackTheme(IOverlayManager om, int userId) {
        OverlayInfo themeInfo = null;
        try {
            themeInfo = om.getOverlayInfo(BLACK_THEMES[0],
                    userId);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return themeInfo != null && themeInfo.isEnabled();
    }

    // Check for the dark notification theme
    public static boolean isUsingDarkNotificationTheme(IOverlayManager om, int userId) {
        OverlayInfo themeInfo = null;
        try {
            themeInfo = om.getOverlayInfo(NOTIFICATION_DARK_THEME, userId);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return themeInfo != null && themeInfo.isEnabled();
    }

    // Check for the black notification theme
    public static boolean isUsingBlackNotificationTheme(IOverlayManager om, int userId) {
        OverlayInfo themeInfo = null;
        try {
            themeInfo = om.getOverlayInfo(NOTIFICATION_BLACK_THEME, userId);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return themeInfo != null && themeInfo.isEnabled();
    }

    // Set light / dark system theme
    public static void setSystemTheme(IOverlayManager om, int userId, boolean useDarkTheme, int darkStyle) {
        // Always unload stock dark theme pre-installed on few devices
        unloadStockDarkTheme(om, userId);

        // Unload dark/black themes if not requested
        if (darkStyle == 1 || !useDarkTheme) unloadDarkTheme(om, userId);
        if (darkStyle == 0 || !useDarkTheme) unloadBlackTheme(om, userId);

        // Ensure dark/black theme enabled if requested
        if (useDarkTheme && darkStyle == 0) {
            for (String theme : DARK_THEMES) {
                try {
                    om.setEnabled(theme, true, userId);
                } catch (RemoteException e) {
                }
            }
        } else if (useDarkTheme && darkStyle == 1) {
            for (String theme : BLACK_THEMES) {
                try {
                    om.setEnabled(theme, true, userId);
                } catch (RemoteException e) {
                }
            }
        }

        // Check black/white accent proper usage
        checkBlackWhiteAccent(om, userId);
    }

    // Set light / dark notification theme
    public static void setNotificationTheme(IOverlayManager om, int userId, boolean useDarkTheme,
                int darkStyle, int notiStyle) {
        if (notiStyle == 1 || (notiStyle == 0 && !useDarkTheme)) {
            unloadDarkNotificationTheme(om, userId);
            unloadBlackNotificationTheme(om, userId);
        } else if (notiStyle == 2 || (notiStyle == 0 && useDarkTheme && darkStyle == 0)) {
            unloadBlackNotificationTheme(om, userId);
            try {
                om.setEnabled(NOTIFICATION_DARK_THEME, true, userId);
            } catch (RemoteException e) {
            }
        } else if (notiStyle == 3 || (notiStyle == 0 && useDarkTheme && darkStyle == 1)) {
            unloadDarkNotificationTheme(om, userId);
            try {
                om.setEnabled(NOTIFICATION_BLACK_THEME, true, userId);
            } catch (RemoteException e) {
            }
        }
    }

    // Check for black and white accent overlays
    public static void checkBlackWhiteAccent(IOverlayManager om, int userId) {
        OverlayInfo themeInfo = null;
        try {
            if (isUsingDarkTheme(om, userId) || isUsingBlackTheme(om, userId)) {
                themeInfo = om.getOverlayInfo(ACCENTS[20],
                        userId);
                if (themeInfo != null && themeInfo.isEnabled()) {
                    om.setEnabled(ACCENTS[20],
                            false /*disable*/, userId);
                    om.setEnabled(ACCENTS[21],
                            true, userId);
                }
            } else {
                themeInfo = om.getOverlayInfo(ACCENTS[21],
                        userId);
                if (themeInfo != null && themeInfo.isEnabled()) {
                    om.setEnabled(ACCENTS[21],
                            false /*disable*/, userId);
                    om.setEnabled(ACCENTS[20],
                            true, userId);
                }
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    // Check for any accent overlay
    public static boolean isUsingAccent(IOverlayManager om, int userId, int accent) {
        OverlayInfo themeInfo = null;
        try {
            themeInfo = om.getOverlayInfo(ACCENTS[accent],
                    userId);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return themeInfo != null && themeInfo.isEnabled();
    }

    // Switches theme accent from one to another or back to stock
    public static void updateAccents(IOverlayManager om, int userId, int accentSetting) {
        if (accentSetting == 0) {
            unloadAccents(om, userId);
        } else if (accentSetting < 20) {
            try {
                om.setEnabled(ACCENTS[accentSetting],
                        true, userId);
            } catch (RemoteException e) {
            }
        } else if (accentSetting > 21) {
            try {
                om.setEnabled(ACCENTS[accentSetting],
                        true, userId);
            } catch (RemoteException e) {
            }
        } else if (accentSetting == 20) {
            try {
                // If using a dark/black theme we use the white accent, otherwise use the black accent
                if (isUsingDarkTheme(om, userId) || isUsingBlackTheme(om, userId)) {
                    om.setEnabled(ACCENTS[21],
                            true, userId);
                } else {
                    om.setEnabled(ACCENTS[20],
                            true, userId);
                }
            } catch (RemoteException e) {
            }
        }
    }

    // Unload all the theme accents
    public static void unloadAccents(IOverlayManager om, int userId) {
        // skip index 0
        for (int i = 1; i < ACCENTS.length; i++) {
            String accent = ACCENTS[i];
            try {
                om.setEnabled(accent,
                        false /*disable*/, userId);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
}
