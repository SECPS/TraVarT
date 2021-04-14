Attribute "width": { 
  description: "Physical width of entity",
  defaultValue: 1.0,
  type: "Number",
  unit: "mm"
}

Attribute "length": {
  description: "Physical length of entity",
  defaultValue: 1.0,
  type: "Number",
  unit: "mm"
}

Attribute "depth": {
  description: "Physical depth of entity",
  defaultValue: 1.0,
  type: "Number",
  unit: "mm"
}

Attribute "force": {
  description: "Force an entity can handle",
  defaultValue: 1.0,
  type: "Number",
  unit: "n"
}

Attribute "torque": {
  description: "...",
  defaultValue: 1.0,
  type: "Number",
  unit: "nm"
}

Attribute "canHandleMinWidth": {
  description: "Min width an entity can handle",
  defaultValue: 0.0,
  type: "Number",
  unit: "mm"
}

Attribute "canHandleMaxWidth": {
  description: "Max width an entity can handle",
  defaultValue: 0.0,
  type: "Number",
  unit: "mm"
}

Attribute "canHandleMinLength": {
  description: "Min length an entity can handle",
  defaultValue: 0.0,
  type: "Number",
  unit: "mm"
}

Attribute "canHandleMaxLength": {
  description: "Max length an entity can handle",
  defaultValue: 0.0,
  type: "Number",
  unit: "mm"
}

Attribute "canHandleMinDepth": {
  description: "Min depth an entity can handle",
  defaultValue: 0.0,
  type: "Number",
  unit: "mm"
}

Attribute "canHandleMaxDepth": {
  description: "Max depth an entity can handle",
  defaultValue: 0.0,
  type: "Number",
  unit: "mm"
}

Attribute "canHandleMinForce": {
  description: "Min force an entity can handle",
  defaultValue: 0.0,
  type: "Number",
  unit: "n"
}

Attribute "canHandleMaxForce": {
  description: "Max force an entity can handle",
  defaultValue: 0.0,
  type: "Number",
  unit: "n"
}

Attribute "canHandleMinTorque": {
  description: "Min torque an entity can handle",
  defaultValue: 0.0,
  type: "Number",
  unit: "nm"
}

Attribute "canHandleMaxTorque": {
  description: "Max torque an entity can handle",
  defaultValue: 0.0,
  type: "Number",
  unit: "nm"
}

Product "Socket": {
  name: "Socket",
  isAbstract: false,
}

Product "Pole1": {
  name: "Pole 1",
  isAbstract: false,
}

Product "Off1": {
  name: "Off 1",
  isAbstract: false, 
}

Product "Neutral": {
  name: "Neutral",
  isAbstract: false, 
}

Product "Rocker1": {
  name: "Rocker 1",
  isAbstract: false, 
}

Product "Rocker2": {
  name: "Rocker 2",
  isAbstract: false, 
}

Product "Changeover": {
  name: "Rocker 2",
  isAbstract: false, 
}

Product "Screw": {
  name: "Screw",
  isAbstract: false,
}

Product "Socket_Layer_1": {
  name: "Socket with Layer 1",
  isAbstract: true,
  childern: [ "Socket", "Pole1"],
  requires: [ "Socket", "Pole1"]
}

Product "Socket_Layer_1_RockSwitch13": {
  name: "Socket with Layer 1",
  isAbstract: false,
  implements: [ "Socket_Layer_1" ],
  childern: [ "Neutral" ],
  requires: [ "Neutral" ]
}

Product "Socket_Layer_1_RockSwitch2": {
  name: "Socket with Layer 1",
  isAbstract: false,
  implements: [ "Socket_Layer_1" ],
}

Product "Socket_Layer_2": {
  name: "Socket with Layer 2",
  isAbstract: true,
  childern: [ "Socket_Layer_1"],
  requires: [ "Socket_Layer_1"],
}

Product "Socket_Layer_2_RockSwitch1": {
  name: "Socket with Layer 2",
  isAbstract: false,
  implements: [ "Socket_Layer_2" ],
  childern: [ "Socket_Layer_1_RockSwitch13", "Rocker2"],
  requires: [ "Socket_Layer_1_RockSwitch13", "Rocker2"],
}

Product "Socket_Layer_2_RockSwitch2": {
  name: "Socket with Layer 2",
  isAbstract: false,
  implements: [ "Socket_Layer_2" ],
  childern: [ "Socket_Layer_1_RockSwitch2", "Rocker1"],
  requires: [ "Socket_Layer_1_RockSwitch2", "Rocker1"],
}

Product "Socket_Layer_2_RockSwitch3": {
  name: "Socket with Layer 2",
  isAbstract: false,
  implements: [ "Socket_Layer_2" ],
  childern: [ "Socket_Layer_1_RockSwitch13", "Rocker1"],
  requires: [ "Socket_Layer_1_RockSwitch13", "Rocker1"],
}

Product "Socket_Layer_3": {
  name: "Socket with Layer 3",
  isAbstract: true,
  childern: [ "Socket_Layer_2", "Screw"],
  requires: [ "Socket_Layer_2", "Screw"],
}

Product "Socket_Layer_3_RockSwitch1": {
  name: "Socket with Layer 2",
  isAbstract: false,
  implements: [ "Socket_Layer_3" ],
  childern: [ "Socket_Layer_2_RockSwitch1"],
}

Product "Socket_Layer_3_RockSwitch2": {
  name: "Socket with Layer 2",
  isAbstract: false,
  implements: [ "Socket_Layer_3" ],
  childern: [ "Socket_Layer_2_RockSwitch2"],
}

Product "Socket_Layer_3_RockSwitch3": {
  name: "Socket with Layer 3",
  isAbstract: false,
  implements: [ "Socket_Layer_3" ],
  childern: [ "Socket_Layer_2_RockSwitch3"],
}

Product "Socket_Layer_4": {
  name: "Socket with Layer 4",
  isAbstract: true,
  childern: [ "Socket_Layer_3"],
  requires: [ "Socket_Layer_3"],
}

Product "Socket_Layer_4_RockSwitch1": {
  name: "Socket with Layer 4",
  isAbstract: false,
  implements: [ "Socket_Layer_4" ],
  childern: [ "Socket_Layer_3_RockSwitch1"],
  requires: [ "Socket_Layer_3_RockSwitch1"],
}

Product "Socket_Layer_4_RockSwitch2": {
  name: "Socket with Layer 4",
  isAbstract: false,
  implements: [ "Socket_Layer_4" ],
  childern: [ "Socket_Layer_3_RockSwitch2"],
  requires: [ "Socket_Layer_3_RockSwitch2"],
}

Product "Socket_Layer_4_RockSwitch3": {
  name: "Socket with Layer 4",
  isAbstract: false,
  implements: [ "Socket_Layer_4" ],
  childern: [ "Socket_Layer_3_RockSwitch3"],
  requires: [ "Socket_Layer_3_RockSwitch3"],
}

Product "RockerSwitch1": {
  name: "RockerSwitch 1",
  isAbstract: false,
  childern: [ "Socket_Layer_4_RockSwitch1" ],
  requires: [ "Socket_Layer_4_RockSwitch1" ],
}

Product "RockerSwitch2": {
  name: "RockerSwitch 2",
  isAbstract: false,
  childern: [ "Socket_Layer_4_RockSwitch2" ],
  requires: [ "Socket_Layer_4_RockSwitch2" ],
}

Product "RockerSwitch3": {
  name: "RockerSwitch 3",
  isAbstract: false,
  childern: [ "Socket_Layer_4_RockSwitch3" ],
  requires: [ "Socket_Layer_4_RockSwitch3" ],
}

Resource "LinefeedSockets": {
  name: "Linefeed Sockets",
  isAbstract: true
}

Resource "LinefeedPoles": {
  name: "Linefeed Poles",
  isAbstract: true
}

Resource "LinefeedRockers": {
  name: "Linefeed Rockers",
  isAbstract: true
}

Resource "LinefeedScrews": {
  name: "Linefeed Screws",
  isAbstract: true
}

Resource "Press": {
  name: "Press",
  isAbstract: true
}

Resource "Screwdriver": {
  name: "Screwdriver",
  isAbstract: true
}

Resource "LinefeedOff": {
  name: "Linefeed Off",
  isAbstract: true
}

Resource "LinefeedType1": {
  name: "Linefeed Typ 1",
  isAbstract: false,
  canHandleMinWidth: 35.0,
  canHandleMaxWidth: 120.0,
  canHandleMinLength: 10.0,
  canHandleMaxLength: 80.0,
  implements: ["LinefeedSockets"]
}

Resource "LinefeedType2": {
  name: "Linefeed Typ 2",
  isAbstract: false,
  canHandleMinWidth: 5.0,
  canHandleMaxWidth: 40.0,
  canHandleMinLength: 2.0,
  canHandleMaxLength: 20.0,
  implements: ["LinefeedPoles", "LinefeedOff"]
}

Resource "LinefeedType3": {
  name: "Linefeed Typ 3",
  isAbstract: false,
  canHandleMinWidth: 1.0,
  canHandleMaxWidth: 10.0,
  canHandleMinLength: 5.0,
  canHandleMaxLength: 60.0,
  implements: ["LinefeedRockers", "LinefeedScrews"]
}

Resource "PressType1": {
  name: "Press Typ 1",
  isAbstract: false,
  canHandleMinDepth: 1.0,
  canHandleMaxDepth: 5.0,
  canHandleMinForce: 5.0,
  canHandleMaxForce: 20
}

Resource "PressType2": {
  name: "Press Typ 2",
  isAbstract: false,
  canHandleMinDepth: 1.0,
  canHandleMaxDepth: 5.0,
  canHandleMinForce: 5.0,
  canHandleMaxForce: 40
}

Resource "PressType3": {
  name: "Press Typ 3",
  isAbstract: false,
  canHandleMinDepth: 49.0,
  canHandleMaxDepth: 10.0,
  canHandleMinForce: 5.0,
  canHandleMaxForce: 40.0,
  implements: ["Press"]
}

Resource "ScrewdriverType1": {
  name: "Screwdriver Typ 1",
  isAbstract: false,
  canHandleMinTorque: 1.0,
  canHandleMaxTorque: 10.0,
  implements: ["Screwdriver"]
}

Resource "ScrewdriverType2": {
  name: "Screwdriver Typ 2",
  isAbstract: false,
  canHandleMinTorque: 20.0,
  canHandleMaxTorque: 100
}