# MovieBot

This is the "Popular Movies" application that has been created in order to meet the requirements for the Udacity - Android
Developer Certification course. As of 4/15/17, the codebase reflects the functionality which meet the requirements for
Stage 1 of the project.

The app makes calls to the MovieDB API in order to retrieve all Movie-related information - https://www.themoviedb.org/

The current functionality of the application is as follows:

<ul>
<li>Upon launch, display a grid arrangement of movie posters</li>
<li>Allow users to change the sort order of the movies via setting (either Most Popular or Top Rated)</li>
<li>Allow user to tap on a specific movie poster, which will transition to a details screen that displays some extra details about the movie</li>
</ul>

<b>Note:</b> Once the repo is cloned/downloaded, you must provide the API key in order to have the application function properly. In order to generate an API key, please see the "How do I apply for an API key" section here: https://www.themoviedb.org/faq/api

Once an API key has been obtained, place it into the strings.xml file (string name = "API_key"), which is found under the res/values directory. [Please click here to see the file.](/app/src/main/res/values/strings.xml)
