# <img alt='Rules' src='https://raw.githubusercontent.com/miguelromeral/PasswordGame/master/app/src/main/res/drawable/instructions.png' height="40" width="auto" /> Game Rules <a href='https://play.google.com/store/apps/details?id=es.miguelromeral.password'><img alt='Get it on Google Play' src='https://play.google.com/intl/en_us/badges/images/generic/en_badge_web_generic.png' height="70" width="auto" /></a>

### <img alt='Words' src='https://raw.githubusercontent.com/miguelromeral/PasswordGame/master/app/src/main/res/drawable/word.png' height="30" width="auto" /> Words (passwords or answers)

This word (password or answer) is **the one that the other players have to guess**. You, as a host, can say any other word in order to be solved by the other players. But hold on! You will have some words (known as hints) that can't be said by the host, so you'll have to find other as related as possible with the answer. **The other players can say as many words as they come to their minds**.

```
e.g.: if the word is "dog", try to say words as "friend", "four paws" or whatever you can imagine but the hints!
```

<img src="https://raw.githubusercontent.com/miguelromeral/PasswordGame/master/screenshots/game_light.png" height="300px" width="auto">

If the team doesn't feel like solving this word, the host can pass to another one before the time finishes, but it will cost some points in the end.

### <img alt='Hints' src='https://raw.githubusercontent.com/miguelromeral/PasswordGame/master/app/src/main/res/drawable/hint.png' height="30" width="auto" /> Hints

They are a list of words related some how with the word that the players have to guess. It makes more interesting (and more difficult too!) the game. **The host is not allowed to say any of them, but the players can.**

```
e.g.: if the word is "Dog", some hints would be "animal", "fur", "stroke", "walk" or even more.
```

**You can also play without hints if you want**. To do that, just go to Settings in the app and disable the option "Play With Hints".

<img src="https://raw.githubusercontent.com/miguelromeral/PasswordGame/master/screenshots/Settings_Hints.png" height="300px" width="auto">

### <img alt='Points' src='https://raw.githubusercontent.com/miguelromeral/PasswordGame/master/app/src/main/res/drawable/star_add.png' height="30" width="auto" /> Points

With every word right you'll obtain some points. You'll get better scores when any of the other players say the word. The host can pass one word if the team is not able to solve it, but it will cost some points.

```
Scores (for each word):
    Correct:
        +200 pts.
        +(Time Bonus)
            <= 5s: 2.000 pts.
            (5s, 30s): Calculated
            >= 30s: 50 pts.
        +(Level Bonus)
            Easy: +0%.
            Medium: +25%.
            Hard: +50%.
    Wrong:
        -100 pts.
```

### <img alt='Time' src='https://raw.githubusercontent.com/miguelromeral/PasswordGame/master/app/src/main/res/drawable/stopwatch_regular.png' height="30" width="auto" /> Time elapsed for each word

The fastest the team can guess the word, the more points will obtain (see table of scores above). Try to think of a good strategy. Sometimes is better pass the word if you're stuck and find a new one (maybe more easy) than struggling with that one and get no points at all.

# Apps Features

### <img alt='Categories' src='https://raw.githubusercontent.com/miguelromeral/PasswordGame/master/app/src/main/res/drawable/category.png' height="30" width="auto" /> Filter by Categories

You can select only words related to one category specified. This way you will not see other topics and you can enjoy your user expierence.

<img src="https://raw.githubusercontent.com/miguelromeral/PasswordGame/master/screenshots/Filter_Category.png" height="300px" width="auto">

### <img alt='Level' src='https://raw.githubusercontent.com/miguelromeral/PasswordGame/master/app/src/main/res/drawable/level.png' height="30" width="auto" /> Filter by levels

You can filter all the words to only get the easiest or even the hardest if you find the mixed options too randomly.

<img src="https://raw.githubusercontent.com/miguelromeral/PasswordGame/master/screenshots/Filter_Level.png" height="300px" width="auto">

### <img alt='Language' src='https://raw.githubusercontent.com/miguelromeral/PasswordGame/master/app/src/main/res/drawable/language.png' height="30" width="auto" /> Different languages

The app's solutions have different languages, depending on the user language preference. For the moment, **only English and Spanish** are supported.

```
To change the language preferences for the solutions fetched from the server, go to Settings (third button on the bottom menu) and look for the option "Language".
```

<img src="https://raw.githubusercontent.com/miguelromeral/PasswordGame/master/screenshots/Settings_Language.png" height="300px" width="auto">

### <img alt='Custom Words' src='https://raw.githubusercontent.com/miguelromeral/PasswordGame/master/app/src/main/res/drawable/list_edit.png' height="30" width="auto" /> Create your own solutions!

If you think the database has no what you need, you can add your own passwords with your custom hints just to challenge your friends. 

**If you like one of the words showed to you from the server, in the final screen you can save them to your password library just clicking the three dots of the solutio in the final screen for that word.**

### <img alt='Server' src='https://raw.githubusercontent.com/miguelromeral/PasswordGame/master/app/src/main/res/drawable/database_regular.png' height="30" width="auto" /> Online database

More than 400 solutions are available in the server. The app's default solutions are available in a database hosted in an online server, so in order to get these words, **you'll need internet connection**. The database's words can change remotely without any user activity, so you can have new content available with no user interaction*.

```
*If there are new categories added, the user will have to update the app to filter  them.
```

Furthermore, you can select to play with only the server answers, your own answers added in your device or even with a mix of them. 

```
To change the source of the solutions, go to Settings and change the "Password Source" value.
```

<img src="https://raw.githubusercontent.com/miguelromeral/PasswordGame/master/screenshots/Settings_Source.png" height="300px" width="auto">

# FAQs

### <img alt='Microphone' src='https://raw.githubusercontent.com/miguelromeral/PasswordGame/master/app/src/main/res/drawable/microphone_regular.png' height="30" width="auto" /> Why do I have to enable the microphone to play?

In fact, **there's no need to have enabled the microphone**. This is a feature added by default in the app. It consists in when the host explains their words to the team, the host can have the ability to enable momentarily to check if he's saying one of the hints not allowed by the password (in that case, the word will be passed automatically). The host has to press the microphone button in the game for each sentence they say.

To use the microphone, you need to allow it explicitly in the App Settings of your Android and change the permissions of "Microphone" to "Allow". If the game checks the option is enabled and it doesn't have this permission from your OS, it'll propmt you a warning and will take you to the App Settings.

```
To allow the microphone permission in your Android device for this app, go to the App called Settings in your list of apps. Then, look for the option "Apps" or "Applications". You'll see a list with all your apps installed. Go down to "Guess Word" and open it. Once you're there, click on the "Permission" option. There you will have the "Microphone" permission opted to "Deny". Click on that permission to change from "Deny" to "Allow". The next time you open the Guess Word app, it will allow you to play without problems.
```

However, if you have this option enabled and don't want to have it, you can disable in the settings of the own app and the microphone button will not be shown during the game and therefore, the warning message won't appear.

```
To disable from the app the Microphone setting and play without that checker, go to the Settings screen in the app (third button in the bottom menu), look for "Microphone" and clear the check for the option.
```

### <img alt='Microphone' src='https://raw.githubusercontent.com/miguelromeral/PasswordGame/master/app/src/main/res/drawable/alert.png' height="30" width="auto" /> Why the words are not being fetched from the server? I can't start the game

If you have selected the option "Password sources" to server or mixed, (the ones that require internet) you may deal with some problems:

1. Check your internet connectivity.
2. If you have internet, start a game with no filters (category nor level)
3. If the game is unable to start or it only shows you your custom answers, then the database of the app in the server doesn't have words available to retrieve.
4. Wait a few moments and try again later or. If the problem persists, it could be caused by an internal failure and will be fixed as soon as possible.

### <img alt='Microphone' src='https://raw.githubusercontent.com/miguelromeral/PasswordGame/master/app/src/main/res/drawable/export.png' height="30" width="auto" /> How can I export and import my previous custom answers?

In screen that shows all your passwords, clicking on the three dots on the right of the toolbar, there are two options enabled: "Import Data" and "Export Data".

<img src="https://raw.githubusercontent.com/miguelromeral/PasswordGame/master/screenshots/export_import.png" height="300px" width="auto">

#### Exporting Data

Once you click that option, all of your current list will be exported if you allow the app giving it the permission to write in the internal storage. If you allow that one, a CSV file will be created in the next folder:

```
*Internal Storage*/Adroid/data/es.miguelromeral.password/files/Documents/mypasswords.csv
```

The file is called **mypasswords.csv** and can be edited by any app or program such as Microsoft Excel or OpenOffice. **CAUTION: if you edit this file, you must keep the format of the rows, otherwise it will bring troubles when importing in the app.**

#### Importing Data

If there is already one file **mypasswords.csv** in the previous folder, if you click on the option "Import Data", the app will automatically save the words for each row in the file in your app library.

If the process couldn't be done, then checks if the file format is correct and have no additional quotes marks in the cells.