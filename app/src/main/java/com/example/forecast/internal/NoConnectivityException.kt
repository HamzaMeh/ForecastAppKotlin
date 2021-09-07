package com.example.forecast.internal

import java.io.IOException

class NoConnectivityException:IOException()
class NoLocationPermissionException:Exception()
class DateNotFoundException:IOException()