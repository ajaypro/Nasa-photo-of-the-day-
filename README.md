# Nasa-photo-of-the-day
App displaying photo/video based on selected date. 

MVVM, Dagger2, Rxjava2, Retrofit, Kotlin

# What can be learnt in this project

1. Usage of commonviewmodel that would be shared by two fragments, how to create viewmodel of same instance when you share with multiple fragments, also by using dagger. 
2. I have created provides method for viewmodel in activity module and fragment module so that it is been shared with same instance, when tried to create viewmodel only in fragment module it would give different instance of viewmodel which recreates everytime when a new fragment is created. In this case it is navigating
from home fragment-> photofragment, homefragment->videofragment which eventually gives the first observed livedata value not the latest data causing irrelevant data flow in  each fragment. 
3. Usage of base fragment that helps in keeping extending fragments with less code.
4. Usage of exoplayer and zoom feature on image after selecting.

# In progress
1. Video restarts when screen orientation changes needs a fix. 

