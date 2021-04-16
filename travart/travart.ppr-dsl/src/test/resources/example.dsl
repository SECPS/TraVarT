Attribute "value": { description: "generic value", defaultValue: 100.0, type: "double" }
Attribute "kwh": { description: "kwh used by the resource", defaultValue: 0.0, type: "double" }
Attribute "minCost": { description: "minimal cost of product", defaultValue: 0.0, type: "double" }



Product "P1": {name: "Cocoa dough", isAbstract: false}

Product "P1": {}

Product "P1" : 
{
	weight: 0.5,
	
}


Product "P1" :
{
	name: "Cocoa dough",
	isAbstract: true
	parents:
	[
		"A", "X",
		"B", "C"
	],
	constraints: [
		{"C1": ""},
		{"C2": ""}, {"C3": ""}
	]
}

Constraint "C1": {definition: "Constraint-1"}
Constraint "C2": {definition: "Constraint-2"}
Constraint "C3": {definition: "Constraint-3"}

Product "P1": {name: "Cocoa dough", isAbstract: false}

Product "P2": {name: "Vanilla dough", isAbstract: false}
Product "P3": {name: "Cocoa Layer", isAbstract: false}
Product "P4": {name: "Vanilla Layer", isAbstract: false}

Resource "S": {name: "Stromkreis"}
Resource "S1": {name: "Stromkreis 1", constraints: [{C2: "descendants all sum(kwh) <= 7000"}], parents: ["S"]}
Resource "S2": {name: "Stromkreis 2", parents: ["S"]}
Resource "R1": {name: "Oven", isAbstract: true, kwh: 5000, parents: ["S1"]}
	
Process "P'1": {
	name: "bake"
	inputs: [
		{productId: "P1", minCost: 10, maxCost: 10},
		{productId: "P2", minCost: 20, maxCost: 20}
	],
	outputs: [
		{OP1: {productId: "P3", weight: 0.5}},
		{OP2: {productId: "P4", weight: 0.5}}
	],
	resources: [
		{resourceId: "R1", minCost: 50, maxCost: 100}
	]
}

Product "P5": {name: "Base cake", isAbstract: false}

Resource "R2": {name: "<A> Pick and place robot", isAbstract: true, parents: ["S2"], constraints: [C5: "-> kn <= 2"]}
Resource "R2Impl": {name: "Pick and place robot 1", isAbstract: false, implements: ["R2"], kn: 2.5} //ERROR because of C5
Resource "R2Impl": {name: "Pick and place robot 1", isAbstract: false, implements: ["R2"], kn: 1.5} //C5: OK

Process "P'2": {
	name: "stack",
	inputs: [
		{productId: "P3", minCost: -, maxCost: -, comesFrom: "OP1"},
		{productId: "P4", minCost: -, maxCost: -, comesFrom: "OP2"}
	],
	outputs: [
		"OP3": {productId: "P5", weight: 1.0}
	],
	resources: [
		{resourceId: "R2", minCost: 150, maxCost: 150}
	],
	parent: -,
	constraints: [
		C10: "R2 -> kn <= 2"
	]
}

Product "P31": {name: "Picked CL", isAbstract: false}
Product "P41": {name: "Picked VL", isAbstract: false}

Process "P'21": {
	name: "pick",
	inputs: [
		{productId: "P3", minCost: -, maxCost: -, comesFrom: "OP1"},
		{productId: "P4", minCost: -, maxCost: -, comesFrom: "OP2"}
	],
	outputs: [
		"OP4": {productId: "P31", weight: 0.5},
		"OP5": {productId: "P41", weight: 0.5}
	],
	resources: [
		{resourceId: "R2", minCost: 100, maxCost: 100}
	],
	constraints : [
		"C7": "R2, P3 -> R2.kn <= P3.maxKn",
		"C8": "R2, P4 -> R2.kn <= P4.maxKn"
	]
	parents: ["P'2"]
}

Process "P'22": {
	name: "place",
	inputs: [
		{productId: "P31", minCost: -, maxCost: -, comesFrom: "OP4"}
		{productId: "P41", minCost: -, maxCost: -, comesFrom: "OP5"}
	]
	outputs: [
		"OP6": {productId: "P5", weight: 1.0}
	]
	resources: [
		{resourceId: "R2", minCost: 50, maxCost: 50}
	]
	parent: "P'2"
}



Product "PA1": {name: "Kuchen", isAbstract: true}

Product "PA2": {name: "Schokokuchen", isAbstract: false, implements: ["PA1"], constraints: [ "CA1": "descendants all sum(weight) <= 50", "CA2": "descendants all count(*) <= 3 ] }

Product "PA3": {name: "Schokoglassur": isAbstract: false, weight: 20, parents: ["PA2"] }
Product "PA4": {name: "Schokoteig": isAbstract: false, weight: 20, parents: ["PA2"] }