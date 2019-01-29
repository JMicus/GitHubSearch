package com.micus.githubsearch

import me.lazmaid.kraph.Kraph

class Query {

    companion object {

        fun create(text: String): String {

            return "{\"query\": \"query { viewer { name } }\""
        }
    }
}