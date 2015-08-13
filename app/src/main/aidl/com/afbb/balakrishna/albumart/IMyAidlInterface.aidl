// IMyAidlInterface.aidl
package com.afbb.balakrishna.albumart;
import com.afbb.balakrishna.albumart.IMyActivtyAidlI;

// Declare any non-default types here with import statements

interface IMyAidlInterface {
   void setName(String name);
   String getName();
   void setReference(com.afbb.balakrishna.albumart.IMyActivtyAidlI reff)
}
