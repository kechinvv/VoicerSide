# VoicerSide

![Build](https://github.com/kechinvv/VoicerSide/workflows/Build/badge.svg)
[![Version](https://img.shields.io/jetbrains/plugin/v/PLUGIN_ID.svg)](https://plugins.jetbrains.com/plugin/PLUGIN_ID)
[![Downloads](https://img.shields.io/jetbrains/plugin/d/PLUGIN_ID.svg)](https://plugins.jetbrains.com/plugin/PLUGIN_ID)


<!-- Plugin description -->
Plugin for editing markdown files by voice. Enable plugin, speak and command via "Doc" to edit file.
Available commands are listed [here](https://github.com/kechinvv/VoicerSide?tab=readme-ov-file#commands). 
Please, check [installation guide](https://github.com/kechinvv/VoicerSide?tab=readme-ov-file#installation) for activation
of voice recognition.

Attention. Currently, only Russian language is supported. English in future releases.

First turn-on in session may take some time. Just wait.
<!-- Plugin description end -->

## Commands

At the moment, a limited number of commands have been implemented:
1. Navigation commands
   * Go to start or end of the document
   * Go to start or end of the line
   * Go to next or preview symbol
2. Editing commands
   * Bold
   * Italic
   * Title and subtitle
   * New line
3. Turn-off recognition 

Examples of command: "Док, новая строка", "Док, пиши курсивом"...

Commands must be pronounced separately. Commands apply to next one sentence.

Aliases for all commands available [here](https://github.com/kechinvv/VoicerSide/blob/main/src/main/resources/voice-commands/ru.json).


## Installation
1. Install plugin
   - Using the IDE built-in plugin system:
  
     <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>Marketplace</kbd> > <kbd>Search for "VoicerSide"</kbd> >
     <kbd>Install</kbd>
  
   - Manually:

     Download the [latest release](https://github.com/kechinvv/VoicerSide/releases/latest) and install it manually using
     <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> > <kbd>Install plugin from disk...</kbd>
2. Download [model.jar](https://github.com/kechinvv/VoicerSide/releases/download/0.0.0/model.jar) 
and model([large](https://alphacephei.com/vosk/models/vosk-model-ru-0.42.zip), [small](https://alphacephei.com/vosk/models/vosk-model-small-ru-0.22.zip) or other)
    - For Windows to `...AppData/Roaming/model-runner`
    - For Linux and Mac to `~/model-runner` 

Contents of `model-runner` folder:
* model.jar
* folder `model` with  model (contents of the folder from the model's archive)

---
Plugin based on the [IntelliJ Platform Plugin Template][template].

[template]: https://github.com/JetBrains/intellij-platform-plugin-template
[docs:plugin-description]: https://plugins.jetbrains.com/docs/intellij/plugin-user-experience.html#plugin-description-and-presentation
