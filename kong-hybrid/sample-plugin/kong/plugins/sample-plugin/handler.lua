local BasePlugin = require "kong.plugins.base_plugin"

local sample = BasePlugin:extend()

sample.VERSION  = "1.0.0"
sample.PRIORITY = 2000

function sample:init_worker()
    sample.super.init_worker(self, "sample-plugin")
end

function sample:access(config)
    sample.super.access(self)

    if kong.request.get_header(config.requestHeader) == nil then
        return sample
    else
        self.req_header_string = kong.request.get_header(config.requestHeader)
    end
end

function sample:header_filter(config)
  -- do custom logic here
  kong.response.set_header("x-sample-response-header", config.responseHeader)
  -- display what we got from the request header
  kong.response.set_header("x-we-got-from-request-header", self.req_header_string)
end

return sample