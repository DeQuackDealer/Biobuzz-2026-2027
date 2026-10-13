{
  "version": "1.5.0",
  "startPoint": { "x": 56, "y": 8, "name": "start", "headingDeg": 90 },
  "lines": [
    {
      "kind": "atomic",
      "id": "path1",
      "name": "Path 1",
      "color": "#E74C3C",
      "endPoint": { "x": 10, "y": 8, "name": "path1" },
      "controlPoints": [],
      "heading": { "type": "constant", "degrees": 180 }
    },
    {
      "kind": "atomic",
      "id": "path2",
      "name": "Path 2",
      "color": "#E67E22",
      "endPoint": { "x": 56, "y": 115, "name": "point2" },
      "controlPoints": [{ "x": 8.4, "y": 83.1521 }],
      "heading": { "type": "constant", "degrees": -90 }
    },
    {
      "kind": "atomic",
      "id": "path3",
      "name": "Path 3",
      "color": "#F1C40F",
      "endPoint": { "x": 47, "y": 138, "name": "point3" },
      "controlPoints": [],
      "heading": { "type": "tangential", "reverse": false }
    },
    {
      "kind": "atomic",
      "id": "path4",
      "name": "Path 4",
      "color": "#2ECC71",
      "endPoint": { "x": 56, "y": 115, "name": "point4" },
      "controlPoints": [],
      "heading": { "type": "constant", "degrees": -90 }
    },
    {
      "kind": "atomic",
      "id": "path5",
      "name": "Path 5",
      "color": "#1ABC9C",
      "endPoint": { "x": 56, "y": 45, "name": "point5" },
      "controlPoints": [],
      "heading": { "type": "tangential", "reverse": false }
    },
    {
      "kind": "atomic",
      "id": "path6",
      "name": "Path 6",
      "color": "#3498DB",
      "endPoint": { "x": 56, "y": 25, "name": "point6" },
      "controlPoints": [],
      "heading": { "type": "constant", "degrees": 90 }
    },
    {
      "kind": "atomic",
      "id": "path7",
      "name": "Path 7",
      "color": "#9B59B6",
      "endPoint": { "x": 5, "y": 106, "name": "point7" },
      "controlPoints": [{ "x": 89.2385, "y": 125.4154 }],
      "heading": { "type": "tangential", "reverse": false }
    }
  ],
  "shapes": [],
  "sequence": [
    { "kind": "path", "lineId": "path1" },
    { "kind": "path", "lineId": "path2" },
    { "kind": "path", "lineId": "path3" },
    { "kind": "path", "lineId": "path4" },
    { "kind": "path", "lineId": "path5" },
    { "kind": "path", "lineId": "path6" },
    { "kind": "path", "lineId": "path7" }
  ]
}
