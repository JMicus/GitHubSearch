package com.micus.githubsearch

import android.app.Instrumentation
import android.content.res.AssetManager
import android.util.Log
import me.lazmaid.kraph.Kraph
import org.json.JSONObject
import org.junit.Test

import org.junit.Assert.*
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.InputStream
import java.io.InputStreamReader

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

    @Test
    fun loadJSON() {

        try {
            //val inputStream: InputStream = AssetManager.AssetInputStream("exampleResponse.json")
            //val inputStreamReader = InputStreamReader(inputStream)

            val sb = StringBuilder()
            var line: String?
            //val br = BufferedReader(inputStreamReader)

            val br = BufferedReader(InputStreamReader(FileInputStream("../app/src/main/assets/exampleResponse.json")))

            line = br.readLine()
            while (br.readLine() != null) {
                sb.append(line)
                line = br.readLine()
            }
            br.close()
            println(sb.toString())

            var json = JSONObject(sb.toString())


        } catch (e:Exception){
            e.printStackTrace()
        }


    }
}
