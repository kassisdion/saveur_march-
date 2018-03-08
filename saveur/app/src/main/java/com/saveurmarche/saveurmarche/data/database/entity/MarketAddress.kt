package com.saveurmarche.saveurmarche.data.database.entity

import io.realm.RealmObject

open class MarketAddress(var formattedAddress: String = "",
                         var zipCode: String = "",
                         var city: String = "",
                         var country: String = "",
                         var longitude: Double = 0.0,
                         var latitude: Double = 0.0) : RealmObject()