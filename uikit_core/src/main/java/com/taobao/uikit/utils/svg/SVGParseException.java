package com.taobao.uikit.utils.svg;

/**
 * Runtime exception thrown when there is a problem parsing an SVG.
 *
 * @author Larva Labs, LLC
 */
public class SVGParseException extends RuntimeException
{

    /**
	 * 
	 */
	private static final long serialVersionUID = 220173845804111714L;

	public SVGParseException(String s)
    {
        super(s);
    }

    public SVGParseException(String s, Throwable throwable)
    {
        super(s, throwable);
    }

    public SVGParseException(Throwable throwable)
    {
        super(throwable);
    }
}
