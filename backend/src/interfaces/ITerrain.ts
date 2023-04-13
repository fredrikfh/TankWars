export interface ITerrain {
    yValues: number[];
    xPoints: number;
    maxY: number;
    generate(): number[];
    getYValues(): number[];
}
