## Contributing
1.  Write your additions, and test them. If you feel that they are ready and work fine, fork this repository with your changes, and submit a Pull Request.
*  Preferrably, write 'clean' code - for example, in cases where you're returning something you could use ternary operators, instead of an if-else statement. If this messes with your head though, it's okay to write it as a if-else.
*  Preferrably don't do unnecessary refactoring, like changing `AutoCrystal` to `CrystalAura`. This also includes changing up words in the README, or adding small bits of text. Most likely, these PR's won't be accepted.

## Building, Issues & Pull Requests
### Local project setup
The client is very simple to build with IntelliJ IDEA, which is the IDE I recommend for developing any sort of mods and this client in specific.
1.  Open `build.gradle` with IntelliJ
2.  This should prompt IntelliJ to ask if you wish to trust this project. Choose `Trust Project` and wait for ForgeGradle and Gradle tasks to be finished.
3.  After this is done, there should be no more changes to the `Project` tab in IntelliJ. You can now run some Gradle tasks to be able to build and work with the project:
*  Go to your top-right in IntelliJ, and click the `Gradle` button. This should open a tab with `Tasks`, `Dependencies`, and `Run Configurations`.
*  Of these options, click `Tasks` and then under that, `forgegradle`.
*  From `forgegradle` you need to run task `setupDecompWorkspace`. This should take some time, and you should wait.
*  After this is done, you can build the client, or optionally contribute/develop it.
### Building
1.  Open page `build` from Gradle > Tasks. To build the client succesfully, you need to run Gradle task `build`.
2.  This will build 2 .jar files. The correct one is `3arthh4ck-<version>-release.jar`, and you can use it by double-clicking it and using the 3arthh4ck installer, or by simply dragging and dropping it to your `.minecraft\mods\` folder.