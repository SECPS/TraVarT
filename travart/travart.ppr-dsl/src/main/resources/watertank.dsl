Attribute "height": {
    description: "Height of an object in mm",
    defaultValue: 1.0,
    type: "double",
    unit: "mm"
}

Attribute "length": {
    description: "Length of an object in mm",
    defaultValue: 1.0,
    type: "double",
    unit: "mm"
}

Attribute "volume": {
    description: "Volume of the object in ccm",
    defaultValue: 1.0,
    type: "double",
    unit: "ccm"
}

Product "Filtertank": {
    name: "Abstract Filtertank",
    isAbstract: true
}

Product "FiltertankM": {
	name: "Fitlertank M",
	implements: ["Fitlertank"],
	volume: 50
}

Product "FiltertankXL": {
	name: "Fitlertank XL",
	implements: ["Fitlertank"],
	volume: 100
}

Product "Charcoal": {
    name: "Abstract Charcoal",
    isAbstract: true
}

Product "BoneCharcoal": {
	name: "Bone Charcoal",
	implements: ["Charcoal"]
}

Product "Activated Charcoal": {
	name: "Bone Charcoal",
	implements: ["Charcoal"]
}

Product "Sand": {
	name: "Bone Charcoal"
}

Product "Freshwatertank": {
    name: "Abstract Freshwatertank",
    isAbstract: true
}

Product "FreshwatertankM": {
	name: "Freshwatertank M",
	implements: ["Freshwatertank"],
	volume: 50
}

Product "FreshwatertankXL": {
	name: "Freshwatertank XL",
	implements: ["Freshwatertank"],
	volume: 100
}

Product "Freshwatervalve": {
	name: "Freshwatervalve"
}

Product "Ironframe": {
	name: "Ironframe"
}

Product "Tube": {
    name: "Abstract Tube",
    isAbstract: true
}

Product "Tube1": {
	name: "Tube 1",
	implements: ["Tube"],
	length: 50
}

Product "Tube2": {
	name: "Tube 2",
	implements: ["Tube"],
	length: 75
}

Product "Tube3": {
	name: "Tube 3",
	implements: ["Tube"],
	length: 100
}

Product "Fibremembrane": {
    name: "Abstract Fibremembrane",
    isAbstract: true
}

Product "Fibremembrane1": {
	name: "Fibremembrane 1",
	implements: ["Fibremembrane"]
}

Product "Fibremembrane2": {
	name: "Fibremembrane 2",
	implements: ["Fibremembrane"]
}

Product "Fibremembrane3": {
	name: "Fibremembrane 3",
	implements: ["Fibremembrane"]
}

Product "Rack": {
    name: "Abstract Rack",
    isAbstract: true
}

Product "Rack1": {
	name: "Rack 1",
	implements: ["Rack"],
	height: 150
}

Product "Rack2": {
	name: "Rack 2",
	implements: ["Rack"],
	height: 200
}

Product "Nanofiltertank": {
    name: "Abstract Nanofiltertank",
    isAbstract: true
}

Product "NanofiltertankM": {
	name: "Nanofiltertank M",
	implements: ["Nanofiltertank"],
	volume: 50
}

Product "NanofiltertankXL": {
	name: "Nanofiltertank XL",
	implements: ["Nanofiltertank"],
	volume: 100
}

Product "Nanofilter": {
	name: "Nanofilter"
}

Product "Wastewatertank": {
    name: "Abstract Wastewatertank",
    isAbstract: true
}

Product "WastewatertankXL": {
	name: "Wastewatertank XL",
	implements: ["Wastewatertank"],
	volume: 100
}

Product "Wastewatervalve": {
	name: "Wastewatervalve"
}

Product "WaterfilterProduct": {
	name: "ForkProduct",
    isAbstract: true,
    requires: [ "FiltertankM", "FiltertankXL", "BoneCharcoal", "ActivatedCharcoal", "Sand", "FreshwatertankM", "FreshwatertankXL", "Freshwatervalve", "Ironframe", "Tube1", "Tube2", "Tube3", "Fibremembrane1", "Fibremembrane2", "Fibremembrane3", "Rack1", "Rack2", "NanofiltertankM", "NanofiltertankXL", "Nanofilter", "WastewatertankXL", "Wastewatervalve"]
}

Product "Waterfilter-Household-2S-BC": {
	name: "Waterfilter-Household-2S-BC",
	requires: [ "FiltertankM", "BoneCharcoal", "Sand", "FreshwatertankM", "Freshwatervalve", "Ironframe", "Tube1", "Tube2", "Fibremembrane1"]
}

Product "Waterfilter-Household-2S-AC": {
	name: "Waterfilter-Household-2S-AC",
	requires: [ "FiltertankM", "ActivatedCharcoal", "Sand", "FreshwatertankM", "Freshwatervalve", "Ironframe", "Tube1", "Tube2", "Fibremembrane1" ]
}

Product "Waterfilter-Household-3S-BC": {
	name: "Waterfilter-Household-3S-BC",
	requires: [ "FiltertankM", "BoneCharcoal", "Sand", "FreshwatertankM", "Freshwatervalve", "Ironframe", "Tube1", "Tube2", "Fibremembrane1", "Rack1", "NanofiltertankM", "Nanofilter" ]
}

Product "Waterfilter-Household-3S-AC": {
	name: "Waterfilter-Household-3S-AC",
	requires: [ "FiltertankM", "ActivatedCharcoal", "Sand", "FreshwatertankM", "Freshwatervalve", "Ironframe", "Tube1", "Tube2", "Fibremembrane1", "Rack1", "NanofiltertankM", "Nanofilter" ]
}	

Product "Waterfilter-School-2S-BC": {
	name: "Waterfilter-School-2S-BC",
	requires: [ "FiltertankXL", "BoneCharcoal", "Sand", "FreshwatertankXL", "Freshwatervalve", "Tube1", "Tube2", "Fibremembrane1", "Fibremembrane2", "Fibremembrane3", "Rack2" ]
}	

Product "Waterfilter-School-2S-AC": {
	name: "Waterfilter-School-2S-AC",
	requires: [ "FiltertankXL", "ActivatedCharcoal", "Sand", "FreshwatertankXL", "Freshwatervalve", "Tube1", "Tube2", "Fibremembrane1", "Fibremembrane2", "Fibremembrane3", "Rack2" ]
}	

Product "Waterfilter-School-3S-BC": {
	name: "Waterfilter-School-3S-BC",
	requires: [ "FiltertankXL", "BoneCharcoal", "Sand", "FreshwatertankXL", "Freshwatervalve", "Tube1", "Tube2", "Tube3", "Fibremembrane1", "Fibremembrane2", "Fibremembrane3", "Rack2", "Nanofilter", "WastewatertankXL", "Wastewatervalve"]
}

Product "Waterfilter-School-3S-AC": {
	name: "Waterfilter-School-3S-AC",
	requires: [ "FiltertankXL", "ActivatedCharcoal", "Sand", "FreshwatertankXL", "Freshwatervalve", "Tube1", "Tube2", "Tube3", "Fibremembrane1", "Fibremembrane2", "Fibremembrane3", "Rack2", "Nanofilter", "WastewatertankXL", "Wastewatervalve"]
}


