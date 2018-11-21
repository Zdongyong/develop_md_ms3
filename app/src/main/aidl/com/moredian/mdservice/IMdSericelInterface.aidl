// IMdSericelInterface.aidl
package com.moredian.mdservice;

// Declare any non-default types here with import statements

interface IMdSericelInterface {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
     String getServiceVersion();
     String getUpdatePassword();
     String getROMversion();
     int updateROM(String fileUrl);
     String getSecretKey();
     String getIMEI();
     boolean setSystemDate(long d);
     int updateROMFromLocal(String fileAbsolutePath);
     long getROMversionCode();
}
