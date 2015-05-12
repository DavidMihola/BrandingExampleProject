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

* Resources are easy: They are merged and those defined in a product flavor source set overlay the ones from the main source set. See above.
* Java classes work a bit differently: All Java files from the main source set are combined with all the Java files found from the flavor source set. This also implies that:
* * You cannot provide a "default implementation" of class in main source set and then optionally replace that with another implementation in the flavor source set. In that case Gradle will complain that you cannot have two implementations of the same class.
* * But you cannot really leave out a class that is only used in some flavors from the main source set. Presuming that the class is instatiated in some code in the main source set, Gradle will complain that some flavor is missing an implementation of that class.
* * So, to summarize: Either provide a class in the main source set and only there. Or leave it out of the main source set and provide it in each and every flavor source set. (An exception to this might be possible with reflection but I have not tried this yet.)
* AndroidManifest.xml: Take care here! Most of our apps use some library for push notifications (either Urban Airship or Parse).
These libraries both declare - and use - their own permissions in the manifest, e. g.:

```
<!-- This app has permission to register with GCM and receive message (UA 5) -->
<permission android:name="com.tailoredapps.permission.C2D_MESSAGE" android:protectionLevel="signature" />
<uses-permission android:name="com.tailoredapps.permission.C2D_MESSAGE" />
```

You *could* put these declarations into the main AndroidManifest.xml and each single app would work just fine. But since multiple apps that declare the same permision
can not be installed on the same device at the same time (from Android L onwards), you would then not be able to install multiple brandings on the same device. We therefore need to move the declaration of these permissions to
the AndroidManifest.xml in the product flavor (and remove them from the main AndroidManifest.xml).

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
but note that this will currently fail on the whitelabel product flavor because that flavor is missing an implementation of ToastClickListener - see the notes above on how Java classes are handled in product flavors
```
