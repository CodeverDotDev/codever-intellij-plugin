Intellij Plugin - Save codelet to Bookmarks.dev
---

**Select code snippet** > **right mouse click** > **Save to Bookmarks.dev**
 
TODO - add gif (create movie)

## Install
Open the project in IntelliJ 

## Deployment
The plugin DevKit provides a simple way to package plugins so we can install and distribute them.
 Right-click the plugin project and select **“Prepare plugin module for Deployment”** (last entry in menu).
  This will generate a JAR file inside the project directory - `bookmarks.dev-intellij-plugin.jar`
  which you can install for testing from local disk:
  
  ![install-plugin-fromdisk](documentation/img/intellij-install-plugin-from-disk.png)

## Publish to JetBrains repository