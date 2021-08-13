local typedefs = require "kong.db.schema.typedefs"

return {
    name = "sample-plugin",
    fields = {
        { consumer = typedefs.no_consumer },
        { config = {
            type = "record",
            fields = {
                {   requestHeader = {
                    type = "string",
                    required = false,
                    default = "x-sample-request-header"
                }},
                {   responseHeader = {
                    type = "string",
                    required = false,
                    default = "this is a sample response header"
                }}
            },
        }}
    }
}