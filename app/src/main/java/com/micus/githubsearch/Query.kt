package com.micus.githubsearch

import me.lazmaid.kraph.Kraph

class Query {

    companion object {

        fun create(text: String): String {

            // TODO example query
            return "{\"query\": \"query { viewer { name } }\""
        }
    }
}