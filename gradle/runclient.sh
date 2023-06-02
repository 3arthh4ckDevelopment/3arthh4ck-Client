#!/bin/bash

#Going into the project folder
cd "$(pwd)"

dir_destination="$(pwd)/run/mods"

dir_origin="$(pwd)/build/libs"

#Checking if gradlew is setup
if [ ! -d "${dir_origin}" ]; then
    echo -e "\n\e[33mNo 3arthh4ck gradlew setup! Creating...\n\e[37m"
    ./gradlew setupDecompWorkspace
fi

#Building the project
echo -e "\e[32m[3arthh4ck]Building the project...\e[37m"
./gradlew build

#Checking if the mods folder exists
if [ -d "${dir_destination}" ]; then
    echo -e "\e[32m[3arthh4ck]Folder found! Moving file...\e[37m"
else
    echo -e "\e[33m[3arthh4ck]Folder not found!! Creating...\e[37m"
    ./gradlew genintellijruns
    mkdir "$(pwd)/run/mods"
fi

#Moving the builded file
mv "${dir_origin}/3arthh4ck-1.8.8-release.jar" "${dir_destination}"
echo -e "\e[35m[3arthh4ck]The file is in the mods folder, Starting the game!\e[37m"

#Starting the game
./gradlew runclient

sleep 3

# NOTE: to use this, you need to import it as script in your IDE, or run it in the main project directory
# TODO: let the python script change this version as well!!