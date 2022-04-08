package com.bink.localhero.model.bakery


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Properties(
    @Json(name = "brands")
    val brands: String,
    @Json(name = "category_tags")
    val categoryTags: String,
    @Json(name = "city")
    val city: String,
    @Json(name = "closed_on")
    val closedOn: String,
    @Json(name = "geometry_type")
    val geometryType: String,
    @Json(name = "iso_country_code")
    val isoCountryCode: String,
    @Json(name = "location_name")
    val locationName: String,
    @Json(name = "naics_code")
    val naicsCode: Int,
    @Json(name = "open_hours")
    val openHours: String,
    @Json(name = "opened_on")
    val openedOn: String,
    @Json(name = "parent_placekey")
    val parentPlacekey: String,
    @Json(name = "phone_number")
    val phoneNumber: String,
    @Json(name = "placekey")
    val placekey: String,
    @Json(name = "postal_code")
    val postalCode: String,
    @Json(name = "region")
    val region: String,
    @Json(name = "safegraph_brand_ids")
    val safegraphBrandIds: String,
    @Json(name = "street_address")
    val streetAddress: String,
    @Json(name = "sub_category")
    val subCategory: String,
    @Json(name = "top_category")
    val topCategory: String,
    @Json(name = "tracking_closed_since")
    val trackingClosedSince: String
)