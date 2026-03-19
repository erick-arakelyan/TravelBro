package com.erikarakelyan.travelbro;

/**
 * ServiceCard — Data model for each travel service card shown in the grid.
 */
public class ServiceCard {

    private final String title;
    private final String subtitle;
    private final int iconResId;      // Drawable resource ID
    private final String bgColorHex;  // Card background color
    private final String accentColorHex; // Icon / accent color

    public ServiceCard(String title, String subtitle,
                       int iconResId, String bgColorHex, String accentColorHex) {
        this.title = title;
        this.subtitle = subtitle;
        this.iconResId = iconResId;
        this.bgColorHex = bgColorHex;
        this.accentColorHex = accentColorHex;
    }

    public String getTitle()           { return title; }
    public String getSubtitle()        { return subtitle; }
    public int    getIconResId()       { return iconResId; }
    public String getBgColorHex()      { return bgColorHex; }
    public String getAccentColorHex()  { return accentColorHex; }
}
