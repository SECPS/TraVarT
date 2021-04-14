Attribute "length": {
    description: "Length of an object in mm",
    defaultValue: 1.0,
    type: "Number",
    unit: "mm"
}
Product "Fork": {
    name: "Fork",
    isAbstract: true
}

Product "Fork3": {
    name: "Fork 3",
    length: 30,
    implements: ["Fork"],
	requires: ["Fork5"]
}
Product "Fork4": {
    name: "Fork 4",
    length: 30,
    implements: ["Fork"],
	requires: ["Fork5"]
}
Product "Fork5": {
    name: "Fork 5",
    implements: ["Fork"],
    length: 50
}

Product "Pipe": {
    name: "Abstract Pipe",
    isAbstract: true
}
Product "Pipe2": {
    name: "Pipe 2",
    implements: ["Pipe"],
    excludes: ["Pipe3", "Pipe8"],
    length: 20
}
Product "Pipe3": {
    name: "Pipe 3",
    implements: ["Pipe"],
    excludes: ["Pipe2", "Pipe8"],
    length: 30
}
Product "Pipe8": {
    name: "Pipe 8",
    implements: ["Pipe"],
    excludes: ["Pipe2", "Pipe3"],
    length: 30
}

Product "Lock": {
    name: "Abstract Lock",
    isAbstract: true,
    requires: ["Pipe"]
}
Product "Lock1": {
    name: "Lock 1",
    implements: ["Lock"],
    length: 30
}
Product "Lock2": {
    name: "Lock 2",
    implements: ["Lock"],
    length: 20
}
Product "Lock3": {
    name: "Lock 3",
    implements: ["Lock"],
    length: 30
}

Product "Barrel1": {
    name: "Barrel 1",
    length:50,
	requires: ["Pipe"]
}

Product "Screw": {
    name:"Screw",
	requires: ["Ring1", "Jack1"]
}

Product "Ring1": {
    name: "Ring 1",
	requires: ["Pipe"]
}

Product "O-Ring": {
    name: "O-Ring",
	requires: ["Ring1", "Jack1"]
}

Product "Jack1": {
    name: "Jack 1",
	requires: ["Pipe"]
}

Product "ForkProduct": {
    name: "ForkProduct",
    isAbstract: true,
    children: [ "Barrel1", "Jack1", "Ring1", "O-Ring", "Fork3", "Fork4", "Fork5", "Lock", "Pipe", "Screw"],
	requires: [ "Barrel1", "Jack1", "Ring1", "O-Ring", "Fork3", "Fork4", "Fork5", "Screw"]
}
Product "Fork-13": {
    name: "Fork 13",
	implements: ["ForkProduct"],
	requires: ["Pipe8", "Lock3"]
}
Product "Fork-2R": {
    name: "Fork 2R",
	implements: ["ForkProduct"],
	requires: ["Pipe3", "Lock2"]
}
Product "Fork-46": {
    name: "Fork 46",
	implements: ["ForkProduct"],
	requires: ["Pipe3", "Lock1"]
}
Product "Fork-57": {
    name: "Fork 57",
	implements: ["ForkProduct"],
	requires: ["Pipe2", "Lock1"]
}