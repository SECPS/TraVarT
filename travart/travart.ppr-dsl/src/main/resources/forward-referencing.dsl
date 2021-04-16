Product "P1": {name: "Water", isAbstract: false}
Product "P1-1": {name: "Malt", isAbstract: true, parents: ["P1"]}
Product "P1-1-1": {name: "Malt", isAbstract: true, parents: ["P1-1"]}
Product "P1-2": {name: "Malt", isAbstract: true, parents: ["P1"]}
Product "P1-2-1": {name: "Malt", isAbstract: true, parents: ["P1-2"]}
Product "P1-2-2": {name: "Malt", isAbstract: true, parents: ["P1-2"]}
Product "P2": {name: "Malt", isAbstract: true, children: ["P2-1", "P2-2"]}
Product "P2-1": {name: "Malt", isAbstract: true, children: ["P2-1-1"]}
Product "P2-1-1": {name: "Malt", isAbstract: true}
Product "P2-2": {name: "Malt", isAbstract: true, children: ["P2-2-1", "P2-2-2"]}
Product "P2-2-1": {name: "Malt", isAbstract: true}
Product "P2-2-2": {name: "Malt", isAbstract: true}