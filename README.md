# MovieBot

This is the "Popular Movies" application that has been created in order to meet the requirements for the Udacity - Android
Developer Certification course. As of 6/12/17, the codebase reflects the functionality which meet the requirements for
both Stage 1 and Stage 2 of the project.

The app makes calls to the MovieDB API in order to retrieve all Movie-related information - https://www.themoviedb.org/

The current functionality of the application is as follows:

<ul>
<li>Upon launch, display a grid arrangement of movie posters</li>
<li>Allow users to change the sort order of the movies via setting (either Most Popular or Top Rated)</li>
<li>Allow user to tap on a specific movie poster, which will transition to a details screen that displays some extra details about the movie</li>
<li>Allow users to view and play trailers (via Youtube)</li>
<li>Allow users to read reviews of selected movies</li>
<li>Allow users to maintain a custom list of Favorite movies</li>
<li>Optimized for tablet support (7" screens)</li>
</ul>

<b>Features of this application:</b>
<ul>
<li>Demonstrate the use of AsyncTasks and AsyncLoaders to run specific functionality off the main UI thread</li>
<li>Handling intents (implicit and explicit) - activity transition, calling another app (i.e. youtube)
<li>Various kinds of Viewgroups and layouts used</li>
<li>Usage of RecyclerViews to demonstrate loading of data into proper UI components efficiently</li>
<li>Network calls and JSON parsing</li>
<li>Using Picasso framework to load and display images from URLs</li>
<li>Data persistence using SQLite DB</li>
<li>Handing configuration changes properly (orientation changes)</li>
<li>Creating layouts to optimize for Tablet users</li> 
</ul>

<b>Note:</b> Once the repo is cloned/downloaded, you must provide the API key in order to have the application function properly. In order to generate an API key, please see the "How do I apply for an API key" section here: https://www.themoviedb.org/faq/api

Once an API key has been obtained, place it into the strings.xml file (string name = "API_key"), which is found under the res/values directory. [Please click here to see the file.](/app/src/main/res/values/strings.xml)
