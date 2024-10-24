# ourladygaga

ourladygaga is a fitness tracker application.
## Installation

#### Required task

- First check is java installed? to check open cmd and run `java --version`. if you see any java version showing then go to the next step else download from [here](https://download.oracle.com/java/23/latest/jdk-23_windows-x64_bin.exe).
- Download JavaFX from [here](https://download2.gluonhq.com/openjfx/23.0.1/openjfx-23.0.1_windows-x64_bin-sdk.zip). unzip the file and you will se a folder move that folder to `C:/Java/javafx-sdk-23.0.1`. if you don't found Java folder Create one
- install git bash from [here](https://github.com/git-for-windows/git/releases/download/v2.47.0.windows.1/Git-2.47.0-64-bit.exe)
- install vs code from [here](https://code.visualstudio.com/sha/download?build=stable&os=win32-x64-user)
- open vs code and add package called "Java extensions for Visual Studio Code" after adding package close vs code.
- download and install mysql database from [here](https://dev.mysql.com/downloads/installer/) if you don't installed mysql database.

![image](https://github.com/user-attachments/assets/5bf636d4-ddf1-4a4d-9b48-4317001790fc)


#### Setup Project

- Go work destination for ex: "Desktop" and create a folder using this name `fitness_app`
- Open this folder with git bash
- Copy this command and clone repo.
```bash
git clone https://github.com/2kilobyte/ourladygaga.git
```
- Go Project folder and open in vscode using this command
```bash
cd ourladygaga && code .
```
- In vs code click on src folder and click App.java and wait until file is loading.
- expand `JAVA PROJECT` from bottom left side and scroll down then add Reference Libiries.

![image](https://github.com/user-attachments/assets/0f7af9b5-706c-4254-8e8f-bae73cab9894)

- Select all jar file and add from JavaFx Sdk that is located in. that migh be in `C:/Java/javafx-sdk-23.0.1/lib` folder.

![image](https://github.com/user-attachments/assets/b6ff4628-4e6b-4039-845b-481633fe1924)

- create a mysql database called `fitness`.
- after creating database run this query to create tables.
```
CREATE TABLE fitness.users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE fitness.daily_activity (
    id INT AUTO_INCREMENT PRIMARY KEY,    
    user_id INT NOT NULL,                  
    activity_data VARCHAR(255) NOT NULL,  
    activity_time INT NOT NULL,            
    activity_date DATE NOT NULL, 
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,          
    FOREIGN KEY (user_id) REFERENCES users(id)  
);
```
- After creating database and table put the database username and password inside `DatabaseConnection.java` file. default mysql username `root`
- Go App.java and scroll down you will see run text button to run application. Just click on that you will see an error poping up.

![image](https://github.com/user-attachments/assets/3abf32d0-0836-42e9-a204-c9403b9f79be)

- after that go .vscode folder and click `launch.json` file and put that code(down below) after `projectName`. Make sure put a comma after projectName
```
"vmArgs": "--module-path \"C:/Java/javafx-sdk-23.0.1/lib\" --add-modules javafx.controls,javafx.fxml"
```

![image](https://github.com/user-attachments/assets/a99589b5-353e-4b9a-b888-084779123c39)

- Go App.java and scroll down you will see run text button to run application. Just click on run. application will show.
