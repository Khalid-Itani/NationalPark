# Parks Explorer (National Parks)

An Android app that loads live data from the National Park Service API and displays a scrollable list of U.S. national parks.

## Features
- Fetches live park data from the National Parks Service API
- Displays parks in a RecyclerView
- Shows park name, location (states), and description
- Loads park images from URLs using Glide

## Demo
![App Demo](media/NationalParks.gif)

## Tech Stack
- Kotlin
- RecyclerView
- CodePath AsyncHttpClient
- Gson
- Glide

## Notes
This project uses the NPS Developer API. If you fork this repo, you should generate your own API key and store it securely (avoid committing secrets).
