package com.micus.githubsearch

import com.beust.klaxon.Klaxon
import com.beust.klaxon.PathMatcher
import java.io.StringReader
import java.lang.Exception
import java.util.regex.Pattern

class ResponseParser {

    companion object {

        fun createRepositories(json: String): MutableList<Repository> {

            var repositoryList: MutableList<Repository> = ArrayList()

            try {

                val pathMatcherCursor = object : PathMatcher {
                    var values: MutableList<String> = ArrayList()
                    override fun pathMatches(path: String) = Pattern.matches(".*data.*search.*edges.*cursor.*", path)
                    override fun onMatch(path: String, value: Any) {
                        values.add(value as String)
                    }
                }

                val pathMatcherName = object : PathMatcher {
                    var values: MutableList<String> = ArrayList()
                    override fun pathMatches(path: String) = Pattern.matches(".*data.*search.*edges.*node.name.*", path)
                    override fun onMatch(path: String, value: Any) {
                        values.add(value as String)
                    }
                }

                val pathMatcherDescription = object : PathMatcher {
                    var values: MutableList<String> = ArrayList()
                    override fun pathMatches(path: String) =
                        Pattern.matches(".*data.*search.*edges.*node.description.*", path)

                    override fun onMatch(path: String, value: Any) {
                        values.add(value as String)
                    }
                }

                val pathMatcherUrl = object : PathMatcher {
                    var values: MutableList<String> = ArrayList()
                    override fun pathMatches(path: String) = Pattern.matches(".*data.*search.*edges.*node.url.*", path)
                    override fun onMatch(path: String, value: Any) {
                        values.add(value as String)
                    }
                }

                val pathMatcherLanguage = object : PathMatcher {
                    var values: MutableList<String> = ArrayList()
                    override fun pathMatches(path: String) =
                        Pattern.matches(".*data.*search.*edges.*node.primaryLanguage.name.*", path)

                    override fun onMatch(path: String, value: Any) {
                        values.add(value as String)
                    }
                }

                val pathMatcherColor = object : PathMatcher {
                    var values: MutableList<String> = ArrayList()
                    override fun pathMatches(path: String) =
                        Pattern.matches(".*data.*search.*edges.*node.primaryLanguage.color.*", path)

                    override fun onMatch(path: String, value: Any) {
                        values.add(value as String)
                    }
                }

                Klaxon().pathMatcher(pathMatcherCursor).parseJsonObject(StringReader(json))
                Klaxon().pathMatcher(pathMatcherName).parseJsonObject(StringReader(json))
                Klaxon().pathMatcher(pathMatcherDescription).parseJsonObject(StringReader(json))
                Klaxon().pathMatcher(pathMatcherUrl).parseJsonObject(StringReader(json))
                Klaxon().pathMatcher(pathMatcherLanguage).parseJsonObject(StringReader(json))
                Klaxon().pathMatcher(pathMatcherColor).parseJsonObject(StringReader(json))

                for (i in 0..pathMatcherName.values.size - 1) {
                    repositoryList.add(
                        Repository(
                            pathMatcherCursor.values[i],
                            pathMatcherName.values[i],
                            pathMatcherUrl.values[i],
                            pathMatcherLanguage.values[i],
                            pathMatcherColor.values[i],
                            pathMatcherDescription.values[i]
                        )
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return repositoryList
        }
    }
}