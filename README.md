Codever Snippets - Intellij Plugin
---

Save ans search snippets from IntelliJ IDEs on [Codever](https://www.codever.land) easily: 

### Save snippet

> **Select code snippet** > **Mouse Right Click** > **Save to Codever**
 
![plugin-usage-showcase](documentation/img/plugin-showcase-save-800x456.gif)

### Search snippets

**Mouse Right Click** > **Save to Codever**

> You can select text in editor, which will be used as **initial value** in input dialog

![plugin-usage-saving-showcase](documentation/img/plugin-showcase-search-800x456.gif)

## Install
Open the project in IntelliJ 

## Deployment
The plugin DevKit provides a simple way to package plugins so we can install and distribute them.
 Right-click the plugin project and select **“Prepare plugin module for Deployment”** (last entry in menu).
  This will generate a JAR file inside the project directory - `codever-intellij-plugin.jar`
  which you can install for testing from local disk:
  
  ![install-plugin-fromdisk](documentation/img/intellij-install-plugin-from-disk.png)

## Publish to JetBrains repository

**Upload Update** in the [overview page](https://plugins.jetbrains.com/plugin/14456-codever-snippets) of the plugin
