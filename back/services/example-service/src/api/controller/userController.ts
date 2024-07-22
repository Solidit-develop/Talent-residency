import express, { Request, Response } from 'express';
import { container } from 'tsyringe';
import { UserService } from '../../services/user/UserService';
import { UserDTO } from '../../shared/dtos/UserDto';

export const userRouter = express.Router();
const userService = container.resolve(UserService);

userRouter.get('/:id', async (req: Request, res: Response) => {
    const userId = parseInt(req.params.id);
    const user = await userService.getUserById(userId);
    res.json(user);
});

userRouter.post('/', async (req: Request, res: Response) => {
    const userDTO: UserDTO = req.body;
    const newUser = await userService.createUser(userDTO);
    res.status(201).json(newUser);
});
