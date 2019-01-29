package com.micus.githubsearch

import me.lazmaid.kraph.Kraph
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    @Test
    fun graphQL() {

        // Query 1 with Kraph -> works
        val query1 = Kraph {
            query {
                field("viewer") {
                    field("name")
                }
            }
        }

        /* Query 2 with Kraph -> doesn't work
            maybe the arguments of "search" or the "on Repository" part should be different
         */
        val query2 = Kraph {
            query {
                fieldObject("search", args = mapOf("query" to "test", "type" to "REPOSITORY", "first" to 3)) {
                    fieldObject("edge") {
                        fieldObject("node") {
                            fieldObject("... on Repository") {
                                field("name")
                            }
                        }
                    }
                }
            }
        }


        /* Query 3 without Kraph
            Output of query 2 changed by hand, but still doesn't work
        */
        val queryString3 = "{\"query\": \"query { search (query: \"test\", type: REPOSITORY, first: 3) { edge { node { ... on Repository { name } } } }\"}"



        println(query2.toGraphQueryString())

        println(query2.toRequestString())

        println(queryString3)
    }
}
