# Disclaimer
This product uses the Federal Emergency Management Agency’s API, but is not endorsed by FEMA.

# Libraries
Published under Apache - 2.0

Retrofit - No changes made to library code
https://github.com/square/retrofit

RxJava - No changes made to library code
https://github.com/ReactiveX/RxJava

RxAndroid - No changes made to library code
https://github.com/ReactiveX/RxAndroid

Dagger - No changes made to library code
https://github.com/google/dagger

Android Open Source Project - No changes made to library code
https://developer.android.com/license


Published under other:

Google Maps Api:
We follow the terms set by:
https://cloud.google.com/maps-platform/terms/

# Data
We abide by the Nominatum Usage Policy:
https://operations.osmfoundation.org/policies/nominatim/

Data gathered from Open Street Map is under the Open Data Commons Open Databse License:
https://opendatacommons.org/licenses/odbl/1.0/index.html

We abide by the terms of use set by the Iowa Department of Transportation:
https://iowadot.gov/policies_and_statements/terms-of-use

We follow the OpenFEMA API Terms & Conditions:
https://www.fema.gov/openfema-api-terms-conditions

# Reasons
Retrofit - Very useful library that uses annotation processors and interfaces to allow for easy and customizable network requests.

RxJava/RxAndroid - Allows for better management of asynchronous operations (mainly the network calls).

Dagger - Dependency injector that uses annotation processors. Creates a dependency graph and we used it mainly useful for object reuse and ensuring objects are singletons.

Google Maps Api - Free if your only displaying a graph, which is what we use it for. Also allows for the generation of heatmaps.

OpenStreetMap/Nominatim - Good alternative to Google's geocoder api. Not as fast, but more reliable in our experience and the license is more permissive.

Iowa DOT Data - We wanted to use government data, and we found this dataset+api combo on data.gov/ It was useful for heatmap generation as it included latitude and longitude values. The heatmap could be made better if the dataset was downloaded to the device, but this would probably be too large for mobile data. Datasets like these could be useful for agents on the go who want to see if clients live in areas with high accident rates. 

FEMA Data - comprehensive api that allows us to give it parameters such as zipcode,state, and city to get back data about damage claims. This could be useful for agents on the go who want to see if clients live in areas with historically large amounts of damage from distasters. 

# High Level Code Explanation

MainActivity - Only screen in the app. All network requests are started and observed here. Contains the GoogleMap View as well as some custom layouts.

IOWAService/FEMAService/NomOSMService - Interfaces that retrofit uses to autogenerate classes. The are used for pull from the IOWA DOT, FEMA, and OpenStreetMap respectively.

models/* - Model classes that contain annotations for GSON to serialize JSON to java objects.

FemaHousingAssistenceAdapter - Recyclerview Adapter that takes a HousingAssistanceOwner object and binds the data to a view, which is then added to a recyclerview.

Util - Contains methods to hide the keyboard and a static final HashMap of state name to abbrevations. Contains methods that could be used for getting locations by search through google geocoder libraries (no longer in use in favor of Open Street Map Nominatim).

FemaNetworkModule/IowaNetworldModule/NominatimOSMNetworkModule (dagger) - modules used by dagger for dependency injection. This is where the objects are provided/created. The objects created here are singletons.

NetworkComponent (dagger) - Combines the above modules into one component that other components can add as a dependency.

MainActivityComponent (dagger) - Allows for the injection of every object in the dependecny graph of its dependencies (NetworkComponent).

StateFarmCompetitionApplication - Application class that initializes the state abbreviation map and the network component.

## Layouts
activity-main - The layout for MainActivity.class. Contains the GoogleMap, FloatingActionButton, and bottom sheet.

damage_summary_item - Layout for the damage items that appear in the aforementioned bottom sheet.

select_search_dialog - Layout for a dialog that pops up letting users select between searching FEMA by zip code or city.

# License
Copyright [2018] [name of copyright owner]

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
