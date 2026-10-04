#include <cstdlib>
#include <fstream>
#include <string>
#include <vector>

#include <android-base/properties.h>
#include <init/init.h>
#include <init/property_service.h>

using android::base::GetProperty;
using android::init::property_set;

void property_override(std::string prop, std::string value) {
    auto pi = (prop_info*) __system_property_find(prop.c_str());
    if (pi != nullptr) {
        __system_property_update(pi, value.c_str(), value.size());
    } else {
        __system_property_add(prop.c_str(), prop.size(), value.c_str(), value.size());
    }
}

// Spoofer Function: Tricks Google Servers into thinking this is a locked, certified Pixel device
void spoof_device_integrity() {
    // 1. Hide Unlocked Bootloader from Banking Apps
    property_override("ro.boot.flash.locked", "1");
    property_override("ro.boot.vbmeta.device_state", "locked");
    property_override("ro.boot.verifiedbootstate", "green");
    
    // 2. Spoof as Pixel 9 Pro XL to pass Play Integrity / SafetyNet
    const std::string fingerprint = "google/komodo/komodo:14/AP1A.240405.002/11480754:user/release-keys";
    
    for (const auto& prop : {"ro.build.fingerprint", "ro.bootimage.build.fingerprint", 
                             "ro.system.build.fingerprint", "ro.vendor.build.fingerprint"}) {
        property_override(prop, fingerprint);
    }
    
    property_override("ro.product.model", "Pixel 9 Pro XL");
    property_override("ro.product.brand", "google");
    property_override("ro.product.name", "komodo");
    property_override("ro.product.device", "komodo");
    property_override("ro.product.manufacturer", "Google");
    
    // 3. Unlock Unlimited Google Photos
    property_override("ro.product.pixel.unlimited_google_photos", "true");
}

void vendor_load_properties() {
    // This executes at the very beginning of the boot phase
    spoof_device_integrity();
    
    // Hardware specific props for veux (Snapdragon 695)
    property_override("ro.vendor.qti.soc_name", "sm6375");
    property_override("ro.vendor.qti.soc_model", "SM6375");
}
