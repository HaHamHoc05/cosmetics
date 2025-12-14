package cosmetic;

public interface InputBoundary<Req extends RequestData, Res extends ResponseData> {
	    void execute(Req request, OutputBoundary<Res> output);

}
