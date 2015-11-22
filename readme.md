#Danielle Vass's Technical Test

![screenshot](http://i.imgur.com/pECOoss.png)

please find in this repo a folder which can be opened using Android Studio to build
an app which should meet the following specification:

1. Display a map showing the users current location in the centre of the screen.
(Leave a gap around the map showing the background).
2. Use the Yahoo Weather API to retrieve the weather for the users current location.
URL: https://developer.yahoo.com/weather/
3. Display the current weather details based on the returned data in a nice format.
4. Set the background colour behind the map in accordance with how warm or cold the
temperature is.
5. Allow the user to pan the map and retrieve the new weather for that location.
6. Handle any errors gracefully.

Min SDK is 14 which is Ice Cream Sandwich

### Known Issues

-[ ] rescans for your current location when you rotate device (just draws another
  map pin on top)
-[ ] The Yahoo Weather API returns weirdly dated data for certain locations e.g.
  5am data when it's actually 10pm.
