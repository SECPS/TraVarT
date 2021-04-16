Attribute "width": { description: "Phsysical width of entity", defaultValue: 1.0, type: "double", unit: "mm" }
Attribute "length": { description: "Phsysical length of entity", defaultValue: 1.0, type: "double", unit: "mm" }
Attribute "depth": { description: "Phsysical depth of entity", defaultValue: 1.0, type: "double", unit: "mm" }
Attribute "force": { description: "Force an entity can handle", defaultValue: 1.0, type: "double", unit: "n" }
Attribute "torque": { description: "...", defaultValue: 1.0, type: "double", unit: "nm" }

Attribute "canHandleMinWidth": { description: "Min width an entity can handle", defaultValue: 0.0, type: "double", unit: "mm" }
Attribute "canHandleMaxWidth": { description: "Max width an entity can handle", defaultValue: 0.0, type: "double", unit: "mm" }
Attribute "canHandleMinLength": { description: "Min length an entity can handle", defaultValue: 0.0, type: "double", unit: "mm" }
Attribute "canHandleMaxLength": { description: "Max length an entity can handle", defaultValue: 0.0, type: "double", unit: "mm" }
Attribute "canHandleMinDepth": { description: "Min depth an entity can handle", defaultValue: 0.0, type: "double", unit: "mm" }
Attribute "canHandleMaxDepth": { description: "Max depth an entity can handle", defaultValue: 0.0, type: "double", unit: "mm" }
Attribute "canHandleMinForce": { description: "Min force an entity can handle", defaultValue: 0.0, type: "double", unit: "n" }
Attribute "canHandleMaxForce": { description: "Max force an entity can handle", defaultValue: 0.0, type: "double", unit: "n" }
Attribute "canHandleMinTorque": { description: "Min torque an entity can handle", defaultValue: 0.0, type: "double", unit: "nm" }
Attribute "canHandleMaxTorque": { description: "Max torque an entity can handle", defaultValue: 0.0, type: "double", unit: "nm" }

Product "Socket": {name: "Socket", isAbstract: false, width: 71.0, length: 71.0, implements: ["LinefeedSockets"], requires: ["Pole1"] }
Product "Pole1": {name: "Pole 1", isAbstract: false, width: 10.0, length: 5.0, depth: 7.0, force:10.0 }
Product "SocketWContacts1": { name: "Socket w Contacts", isAbstract: false, width: 71.0, length: 71.0 }
Product "Rocker2": { name: "Rocker 2", isAbstract: false, width: 3.0, length: 30.0, depth:5.0, force:15.0 }
Product "SocketWContacts2": { name: "Socket w Contacts", isAbstract: false, width: 71.0, length: 71.0 }
Product "Screw": { name: "Screw", isAbstract: false, width:2.0, length:10.0, torque:3.0 }
Product "SocketWContacts3": { name: "Socket w Contacts", isAbstract: false, width: 71.0, length: 71.0 }
Product "SocketWContacts4": { name: "Socket w Contacts", isAbstract: false, width: 71.0, length: 71.0 }
Product "Off1": { name: "Off 1", isAbstract: false, width: 10.0, length: 4.0, depth:2.0, force:10.0 }

Resource "LinefeedSockets": {name: "Linefeed Sockets", isAbstract: true}
Resource "LinefeedPoles": {name: "Linefeed Poles", isAbstract: true}
Resource "LinefeedRockers": {name: "Linefeed Rockers", isAbstract: true}
Resource "LinefeedScrews": {name: "Linefeed Screws", isAbstract: true}
Resource "Press": {name: "Press", isAbstract: true}
Resource "Screwdriver": {name: "Screwdriver", isAbstract: true}
Resource "LinefeedOff": {name: "Linefeed Off", isAbstract: true}

Resource "LinefeedType1": {name: "Linefeed Typ 1", isAbstract: false, canHandleMinWidth: 35, canHandleMaxWidth: 120, canHandleMinLength: 10, canHandleMaxLength: 80, implements: ["LinefeedSockets"]}
Resource "LinefeedType2": {name: "Linefeed Typ 2", isAbstract: false, canHandleMinWidth: 5, canHandleMaxWidth: 40, canHandleMinLength: 2, canHandleMaxLength: 20, implements: ["LinefeedPoles", "LinefeedOff"]}
Resource "LinefeedType3": {name: "Linefeed Typ 3", isAbstract: false, canHandleMinWidth: 1, canHandleMaxWidth: 10, canHandleMinLength: 5, canHandleMaxLength: 60, implements: ["LinefeedRockers", "LinefeedScrews"]}
Resource "PressType1": {name: "Press Typ 1", isAbstract: false, canHandleMinDepth: 1, canHandleMaxDepth: 5, canHandleMinForce: 5, canHandleMaxForce: 20}
Resource "PressType2": {name: "Press Typ 2", isAbstract: false, canHandleMinDepth: 1, canHandleMaxDepth: 5, canHandleMinForce: 5, canHandleMaxForce: 40}
Resource "PressType3": {name: "Press Typ 3", isAbstract: false, canHandleMinDepth: 4.9, canHandleMaxDepth: 10, canHandleMinForce: 5, canHandleMaxForce: 40, implements: ["Press"]}
Resource "ScrewdriverType1": {name: "Screwdriver Typ 1", isAbstract: false, canHandleMinTorque: 1, canHandleMaxTorque: 10,  implements: ["Screwdriver"]}
Resource "ScrewdriverType2": {name: "Screwdriver Typ 2", isAbstract: false, canHandleMinTorque: 20, canHandleMaxTorque: 100}

Process "Insert": {
	name: "Insert",
	isAbstract: false,
	inputs: [
		{productId: "Socket", minCost: 10, maxCost: 20},
		{productId: "Pole1", minCost: 30, maxCost: 40}
	],
	outputs: [
		{SwC1: {productId: "SocketWContacts1", costWeight: 1.0}}
	],
	resources: [
		{resourceId: "LinefeedSockets", minCost: 50, maxCost: 100},
		{resourceId: "LinefeedPoles", minCost: 50, maxCost: 100}
	]
}

Process "Insert/Press": {
	name: "Insert/Press",
	isAbstract: false,
	inputs: [
		{productId: "SocketWContacts1", comesFrom: "SwC1"},
		{productId: "Rocker2", minCost: 30, maxCost: 40}
	],
	outputs: [
		{SwC2: {productId: "SocketWContacts2", costWeight: 1.0}}
	],
	resources: [
		{resourceId: "LinefeedRockers", minCost: 50, maxCost: 100},
		{resourceId: "Press", minCost: 50, maxCost: 100}
	]
}

Process "Insert/Screw": {
	name: "Insert/Press",
	isAbstract: false,
	inputs: [
		{productId: "SocketWContacts2", comesFrom: "SwC2"},
		{productId: "Screw", minCost: 30, maxCost: 40}
	],
	outputs: [
		{SwC3: {productId: "SocketWContacts3", costWeight: 1.0}}
	],
	resources: [
		{resourceId: "LinefeedScrews", minCost: 50, maxCost: 100},
		{resourceId: "Screwdriver", minCost: 50, maxCost: 100}
	]
}

Process "Insert/Press2": {
	name: "Insert/Press",
	isAbstract: false,
	inputs: [
		{productId: "SocketWContacts3", comesFrom: "SwC3"},
		{productId: "Off1", minCost: 30, maxCost: 40}
	],
	outputs: [
		{SwC4: {productId: "SocketWContacts4", costWeight: 1.0}}
	],
	resources: [
		{resourceId: "LinefeedOff", minCost: 50, maxCost: 100},
		{resourceId: "Press", minCost: 50, maxCost: 100}
	]
}

Constraint "C1": {definition: "LinefeedSockets subtype all -> all.canHandleMinWidth < Socket.width and all.canHandleMaxWidth > Socket.width"}
Constraint "C2": {definition: "LinefeedSockets subtype all -> all.canHandleMinLength < Socket.length and all.canHandleMaxLength > Socket.length"}
Constraint "C3": {definition: "LinefeedPoles subtype all -> all.canHandleMinWidth < Pole1.width and all.canHandleMaxWidth > Pole1.width"}
Constraint "C4": {definition: "LinefeedPoles subtype all -> all.canHandleMinLength < Pole1.length and all.canHandleMaxLength > Pole1.length"}
Constraint "C5": {definition: "LinefeedRockers subtype all -> all.canHandleMinWidth < Rocker2.width and all.canHandleMaxWidth > Rocker2.width"}
Constraint "C6": {definition: "LinefeedRockers subtype all -> all.canHandleMinLength < Rocker2.length and all.canHandleMaxLength > Rocker2.length"}
Constraint "C7": {definition: "Press subtype all -> all.canHandleMinDepth < Rocker2.depth and all.canHandleMaxDepth > Rocker2.depth"}
Constraint "C8": {definition: "Press subtype all -> all.canHandleMinForce < Rocker2.force and all.canHandleMaxForce > Rocker2.force"}
Constraint "C9": {definition: "LinefeedScrews subtype all -> all.canHandleMinWidth < Screw.width and all.canHandleMaxWidth > Screw.width"}
Constraint "C10": {definition: "LinefeedScrews subtype all -> all.canHandleMinLength < Screw.length and all.canHandleMaxLength > Screw.length"}
Constraint "C11": {definition: "Screwdriver subtype all -> all.canHandleMinTorque < Screw.torque and all.canHandleMaxTorque > Screw.torque"}
Constraint "C12": {definition: "LinefeedOff subtype all -> all.canHandleMinWidth < Off1.width and all.canHandleMaxWidth > Off1.width"}
Constraint "C13": {definition: "LinefeedOff subtype all -> all.canHandleMinLength < Off1.length and all.canHandleMaxLength > Off1.length"}
Constraint "C14": {definition: "Press subtype all -> all.canHandleMinDepth < Off1.depth and all.canHandleMaxDepth > Off1.depth"}
Constraint "C15": {definition: "Press subtype all -> all.canHandleMinForce < Off1.force and all.canHandleMaxForce > Off1.force"}