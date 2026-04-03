declare const _sfc_main: import("vue").DefineComponent<{
    canvasWidth: {
        type: NumberConstructor;
        default: number;
    };
    canvasHeight: {
        type: NumberConstructor;
        default: number;
    };
    show: {
        type: BooleanConstructor;
        default: boolean;
    };
    puzzleScale: {
        type: NumberConstructor;
        default: number;
    };
    sliderSize: {
        type: NumberConstructor;
        default: number;
    };
    range: {
        type: NumberConstructor;
        default: number;
    };
    imgs: {
        type: ArrayConstructor;
        default: null;
    };
    successText: {
        type: StringConstructor;
        default: string;
    };
    failText: {
        type: StringConstructor;
        default: string;
    };
    sliderText: {
        type: StringConstructor;
        default: string;
    };
}, {
    rangeSlider: import("vue").Ref<HTMLDivElement | undefined>;
    canvas1: import("vue").Ref<HTMLCanvasElement | undefined>;
    canvas2: import("vue").Ref<HTMLCanvasElement | undefined>;
    canvas3: import("vue").Ref<HTMLCanvasElement | undefined>;
    emit: (event: "success" | "fail" | "close", ...args: any[]) => void;
    props: any;
    state: {
        mouseDown: boolean;
        startWidth: number;
        startX: number;
        newX: number;
        pinX: number;
        pinY: number;
        loading: boolean;
        isCanSlide: boolean;
        error: boolean;
        infoBoxShow: boolean;
        infoText: string;
        infoBoxFail: boolean;
        timer1: {
            hasRef: () => boolean;
            refresh: () => NodeJS.Timeout;
            [Symbol.toPrimitive]: () => number;
            ref: () => NodeJS.Timeout;
            unref: () => NodeJS.Timeout;
        } | undefined;
        closeDown: boolean;
        isSuccess: boolean;
        imgIndex: number;
        isSubmting: boolean;
    };
    styleWidth: import("vue").ComputedRef<number>;
    puzzleBaseSize: import("vue").ComputedRef<number>;
    sliderBaseSize: import("vue").ComputedRef<number>;
    onC: () => void;
    onCloseMouseDown: () => void;
    onCloseMouseUp: () => void;
    onRangeMouseDown: (e: Event) => void;
    onRangeMouseMove: (e: Event) => void;
    onRangeMouseUp: () => void;
    init: (withCanvas?: boolean) => void;
    getRandom: (min: number, max: number) => number;
    makeImgSize: (img: HTMLImageElement) => number[];
    paintBrick: (ctx: CanvasRenderingContext2D) => void;
    makeImgWithCanvas: () => string;
    submit: () => void;
    resetState: () => void;
    reset: () => void;
}, unknown, {}, {}, import("vue").ComponentOptionsMixin, import("vue").ComponentOptionsMixin, ("success" | "fail" | "close")[], "success" | "fail" | "close", import("vue").VNodeProps & import("vue").AllowedComponentProps & import("vue").ComponentCustomProps, Readonly<import("vue").ExtractPropTypes<{
    canvasWidth: {
        type: NumberConstructor;
        default: number;
    };
    canvasHeight: {
        type: NumberConstructor;
        default: number;
    };
    show: {
        type: BooleanConstructor;
        default: boolean;
    };
    puzzleScale: {
        type: NumberConstructor;
        default: number;
    };
    sliderSize: {
        type: NumberConstructor;
        default: number;
    };
    range: {
        type: NumberConstructor;
        default: number;
    };
    imgs: {
        type: ArrayConstructor;
        default: null;
    };
    successText: {
        type: StringConstructor;
        default: string;
    };
    failText: {
        type: StringConstructor;
        default: string;
    };
    sliderText: {
        type: StringConstructor;
        default: string;
    };
}>> & {
    onSuccess?: ((...args: any[]) => any) | undefined;
    onFail?: ((...args: any[]) => any) | undefined;
    onClose?: ((...args: any[]) => any) | undefined;
}, {
    canvasWidth: number;
    canvasHeight: number;
    show: boolean;
    puzzleScale: number;
    sliderSize: number;
    range: number;
    imgs: unknown[];
    successText: string;
    failText: string;
    sliderText: string;
}>;
export default _sfc_main;
