package com.plaglabs.pillreminder.app;

/**
 * Created by plagueis on 30/04/14.
 */
public interface NavDrawerItem {
    public int getId();
    public String getLabel();
    public int getType();
    public boolean isEnabled();
    public boolean updateActionBarTitle();
}
