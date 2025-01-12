## Points Management System

This is a coding exercise displaying this list of items to the user with requirements accordingly. This project implements a RESTful API for managing user points across various payers.

### Objectives

Display all the items grouped by "listId"

Sort the results first by "listId" then by "name" when displaying.

Filter out any items where "name" is blank or null.

The final result should be displayed to the user in an easy-to-read list.

### Environment Setup

Android Studio (Git/VCS, Gradle version 8.0)

Android SDK (14, API level 34)

Mobile Device/Emulator (Pixel 3a API 34)

### Launch the App / User Guide

Simply Clone this [repository](https://github.com/dl4035/Points-Management-System/tree/master)  or download the project .zip file and explore in your local Android Studio, and examine the emulator view after sync and build the project.

### Screen Recording demo

![](https://github.com/dl4035/Points-Management-System/blob/master/demo0.gif)


## Updated Features

- **Add Transactions**: Incorporate transactions with payer details, point values, and timestamps.
- **Spend Points**: Allocate points based on the oldest transaction dates, ensuring fair distribution.
- **View Balances**: Retrieve the current point balance for each payer.

## Prerequisites

- Python 3.8 or higher
- Flask

## Installation

1. **Clone the Repository**:

   ```bash
   git clone https://github.com/dl4035/Points-Management-System.git
   cd Points-Management-System
   ```

2. **Set Up a Virtual Environment**:

   ```bash
   python3 -m venv venv
   source venv/bin/activate  # On Windows: venv\Scripts\activate
   ```

3. **Install Dependencies**:

   ```bash
   pip install -r requirements.txt
   ```

## Usage

1. **Start the Server**:

   ```bash
   flask run
   ```

2. **API Endpoints**:

   - **Add Transaction**:
     - **Endpoint**: `POST /add_transaction`
     - **Description**: Registers a new transaction.
     - **Request Body**:

       ```json
       {
         "payer": "COMPANY_NAME",
         "points": 1000,
         "timestamp": "2023-01-01T10:00:00Z"
       }
       ```

   - **Spend Points**:
     - **Endpoint**: `POST /spend`
     - **Description**: Deducts points using the oldest transactions first.
     - **Request Body**:

       ```json
       {
         "points": 5000
       }
       ```

   - **View Balances**:
     - **Endpoint**: `GET /balances`
     - **Description**: Returns the current point balances per payer.

## Testing

Execute the following command to run tests:

```bash
python -m unittest discover
```

## License

This project is licensed under the MIT License.

