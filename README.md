### mindustry-captcha-plugin

* This is a test implementation. To use it, you need to fork the plugin and implement logic for transferring the player to the main server!!!
* The plugin adds a simple captcha to (probably) protect the main server from bots.
* If there are any issues, please report them in the issue; however, there is a 90% chance I won’t care. i coded this in 3 hours. just out of boredom.

### Usage

* `./gradlew jar`
* Move the built plugin into `config/mods` and start the server

### Note

* There is an option to enable/disable randomization of the background behind letters
* do `config randomizeTiles true/false` (default false)

### Preview
* <img width="1057" height="495" alt="norandomtiles" src="https://github.com/user-attachments/assets/26c051a3-15f0-41c4-9585-c6e7aa86140f" />
* <img width="1408" height="693" alt="randomizedtiles" src="https://github.com/user-attachments/assets/481e4234-c32a-4a86-ba61-9e09be7af11d" />
* <img width="273" height="273" alt="codes" src="https://github.com/user-attachments/assets/6f0153a4-8a84-4f3b-9cca-8545204c3ac1" />
