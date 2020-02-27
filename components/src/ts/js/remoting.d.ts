export declare class CallBuilder {
    path: string;
    private connector;
    constructor(path: string);
    inputValue(id: string): void;
    stringValue(name: string, value: string): void;
    numberValue(name: string, value: number): void;
    pathAndQuery(): string;
    post(): void;
    get(): void;
}
