Please add the following to you local.properties file.
EXCHANGE_RATE_API_KEY = 4355559f6761df5c780bb287da4a93e5
BASE_URL = http://api.exchangeratesapi.io

# **Currency Conversion Application**
This is currency conversion application which built upon the concept of clean MVVM architecture using Android Architectural Components. Following Restful webservice has been used for API http://api.exchangeratesapi.io

It has been built using the following JetPack Components:
● Jet Pack Compose.
● Hilt.
● Navigation Compose.
● Room.

The application has been built using clean architecture following layer:

● Data.
● Domain.
● Presentation.

**Data**
It is responsible for providing data to domain layer. It contains respositories for providing data from API and Database layer.

**Domain**
It is responsible for defining user actions, which are the usecases. 

**Presentation**
Presentation layer business logic for the actions that user want to perform.

# Requirements:

● The app should use Jetpack Compose for UI development.
● The app should use a Currency Rates API to fetch the latest exchange rates. (Done)
● The app should allow users to select a base currency and one or more target currencies
for conversion.
● The app should display the latest exchange rates for the selected currencies and update
them automatically at regular intervals.
● The user should be able to enter a custom amount to convert and see the result in
real-time.
● The app should handle network errors and display appropriate error messages to the
user.
● The app should be responsive and provide a smooth user experience.
● The app should be well-documented and include instructions for building and running the
app.
● The currency rat.s should be cached and only updated once a day.

# Optional Requirements:

● The app can provide a historical exchange rate graph for the selected currencies.
● The app can allow users to save their favorite currency pairs for quick access.
● The app can include a currency calculator for simple calculations.
● The app can provide a widget for quick access to the latest exchange rates.
● KMM Implementation for the app.


