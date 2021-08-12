local BasePlugin = require "kong.plugins.base_plugin"

local sample = BasePlugin:extend()

sample.VERSION  = "1.0.0"
sample.PRIORITY = 2000

function sample:init_worker()
    sample.super.init_worker(self, "sample-plugin")
end

function sample:access(config)
    sample.super.access(self)
    math.randomseed(os.time())
    local a = kong.request.get_header(config.requestHeader)
    if a ~= config.startValue then
        kong.response.exit(426, {message = "Incorrect number", answer = config.startValue, myValue = a})
    else
        local value1 = math.random(1, 15)
        local value2 = math.random(1, 15)
        kong.response.set_header("value1", value1)
        kong.response.set_header("value2", value2)
        config.startValue = tostring(value1 + value2)
    end
end

return sample