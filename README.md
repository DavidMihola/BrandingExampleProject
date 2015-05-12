# BrandingExampleProject

This example project demonstrates how to create multiple brandings from a white-label app using Gradle's "product flavors".

### Why product flavors?

I was about to write a short introduction about why we are using product flavors for the branding -
but then I found this blog post, which says everything very clearly:
http://blog.robustastudio.com/mobile-development/android/building-multiple-editions-of-android-app-gradle/

Official documentation:

http://tools.android.com/tech-docs/new-build-system/user-guide#TOC-Product-flavors

> The following rules are used when dealing with all the sourcesets used to build a single APK:
> * All source code (src/*/java) are used together as multiple folders generating a single output.
> * Manifests are all merged together into a single manifest. This allows Product Flavors to have different components and/or permissions, similarly to Build Types.
> * All resources (Android res and assets) are used using overlay priority where the Build Type overrides the Product Flavor, which overrides the main sourceSet.
> * Each Build Variant generates its own R class (or other generated source code) from the resources. Nothing is shared between variants.

A few important points:

* Resources are easy - they are merged and those defined in a product flavor source set overlay the ones from the main source set. See above.
* AndroidManifest.xml: Take care here! Most of our apps use some library for push notifications (either Urban Airship or Parse).
These libraries both declare - and use - their own permissions in the manifest, e. g.:

```
<!-- This app has permission to register with GCM and receive message (UA 5) -->
<permission android:name="com.tailoredapps.permission.C2D_MESSAGE" android:protectionLevel="signature" />
<uses-permission android:name="com.tailoredapps.permission.C2D_MESSAGE" />
```

But since multiple apps that declare the same permision
can not be installed on the same device at the same time (from Android L onwards), we need to move the declaration of these permissions to
the AndroidManifest.xml in the product flavor.

### How to build it?

Building the different brandings works especially well with this setup:

```
In the project source directory (BrandingExampleProject):

* to build one specific combination of build type (debug/release) and product flavor (good/evil):
./gradlew assembleGoodDebug
./gradlew assembleEvilRelease

* to build all build types of one product flavor
./gradlew assembleGood
./gradlew assembleEvil

* to build one build type for each product flavor
./gradlew assembleDebug
./gradlew assembleRelease

* to build everything
./gradlew assemble
```
