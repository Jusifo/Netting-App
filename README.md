# Netting-App

A command-line tool that calculates the simplest way to settle debts among a group of people. This project demonstrates a netting algorithm in Java, built with Gradle.

## How to Run (For Recruiters & Testers)

The easiest way to try this application is to download the pre-built executable JAR file.

1.  **Go to the [Releases Page](https://github.com/YOUR_USERNAME/YOUR_REPOSITORY/releases)** on the right side of this repository's main page.
2.  Download the `netting-all.jar` file from the latest release.
3.  Open a terminal (or Command Prompt on Windows).
4.  Navigate to the directory where you downloaded the file, for example:
    ```bash
    cd Downloads
    ```
5.  Run the application with the following command:
    ```bash
    java -jar netting-all.jar
    ```

_**Prerequisite:** You need to have Java 17 (or newer) installed on your system._

## How to Build from Source (For Developers)

If you want to build the project yourself, follow these steps:

1.  Clone the repository:
    ```bash
    git clone https://github.com/YOUR_USERNAME/YOUR_REPOSITORY.git
    ```
2.  Navigate into the project directory:
    ```bash
    cd Netting-App
    ```
3.  Build the executable JAR using the Gradle wrapper:
    ```bash
    ./gradlew shadowJar
    ```
4.  The final JAR file will be located at `app/build/libs/netting-all.jar`. You can run it using the command from the section above.