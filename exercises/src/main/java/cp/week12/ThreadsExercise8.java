package cp.week12;

/**
 *
 * @author Fabrizio Montesi <fmontesi@imada.sdu.dk>
 */
public class ThreadsExercise8
{
	/*
	- As ThreadExercise7, but now use a global counter among all threads instead.
	- Reason about the pros and cons of the two concurrency strategies
	  (write them down).

	- Individual counter
		- Pros
			- Can be faster because there is no need for sync, tho not that much because it is so simple the operation.
			- Local vaiables - easier to avoid race conditions

		- Cons
			- Can be more complicated to implement.
			- Maybe too overkill in some situations
			- Needs more memory

	- Global counter
		- Pros
			- Easy to interact with
			- value always available

		- Cons
			- If not done properly, there is a bigger chance of race condition.
			- can be slower in case of an advanced structure interacted with a lot.
	*/
}
