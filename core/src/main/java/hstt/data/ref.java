package hstt.data;
//----------------------------------------------------------------------------------------
//	Copyright ? 2006 - 2010 Tangible Software Solutions Inc.
//	This class can be used by anyone provided that the copyright notice remains intact.
//
//	This class is used to simulate the ability to pass arguments by reference in Java.
//----------------------------------------------------------------------------------------
public final class ref<T>
{
	public T v;
	public ref(T refarg)
	{
		v = refarg;
	}
}