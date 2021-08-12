local PLUGIN_NAME = "sample-plugin"


-- helper function to validate data against a schema
local validate do
  local validate_entity = require("spec.helpers").validate_plugin_config_schema
  local plugin_schema = require("kong.plugins."..PLUGIN_NAME..".schema")

  function validate(data)
    return validate_entity(data, plugin_schema)
  end
end


describe(PLUGIN_NAME .. ": (schema)", function()


  it("Invalid Params", function()
    local ok, err = validate({
        requestHeader = true,
        startValue = false,
      })

    assert.is_same({
      ["config"] = {
        ["requestHeader"] = "expected a string",
        ["startValue"] = "expected a string"
      }
    }, err)
    assert.is_falsy(ok)
  end)


  it("valid Params", function()
    local ok, err = validate({
        requestHeader = "yourValue",
        startValue = "startValue",
      })
    assert.is_nil(err)
    assert.is_truthy(ok)
  end)


end)
