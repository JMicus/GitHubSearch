package com.micus.githubsearch



class Query {

    companion object {

        fun create(query: String, results: String, direction: String, cursor: String): String {

            // TODO example query


            //var query = "{ viewer { name } }"

            //var query = "{ search (query:Example, type:REPOSITORY, first:2) { edges { cursor }}}"

            var first_last = "first"
            if (direction.equals("before")) first_last = "last"


            var queryString = """{
                  search (
                    query:$query,
                    type:REPOSITORY,
                    $first_last:$results,
                    $direction:${cursor.replace("=", "")}
                  ) {
                    edges {
                      cursor
                      node {
                        ... on Repository {
                          name
                          description
                          url
                          primaryLanguage {
                            color
                            name
                          }
                        }
                      }
                    }
                  }
                }
                """.replace("\n", "")

            return "{\"query\": \"$queryString\""
        }
    }
}