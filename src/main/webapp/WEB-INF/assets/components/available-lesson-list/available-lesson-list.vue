<template>
    <div>
        <h3 class="title">Available lessons</h3>
        <ul :class="$style.list" v-if="data.length > 0">
            <li v-for="element in data">
                <div class="row">
                    <div class="col-9">
                        <p :class="$style.title">{{ element.course.title }}</p>
                        <p :class="$style.counter">{{ element.lessons.length }} {{ element.lessons.length === 1 ? 'teacher' : 'teachers' }} available</p>
                    </div>
                    <div :class="$style.controls + ' col-3'">
                        <a class="button" href="javascript:void(0);" v-on:click="toggleDetails"><i class="fas fa-calendar-week"></i><span class="d-none d-sm-inline-block">&nbsp;Details</span></a>
                    </div>
                </div>
                <div :class="$style.details" data-open="false">
                    <ul :class="$style.lessons">
                        <li v-for="lesson in element.lessons">
                            <p :class="$style.teacher">{{ lesson.teacher.name + ' ' + lesson.teacher.surname }}</p>
                            <ul :class="$style.slots">
                                <li v-for="slot in lesson.slots" :data-available="slot.available" :data-is-mine="slot.isMine" :data-day="slot.day" :data-hour="slot.hour" :data-tid="lesson.teacher.id" :data-cid="element.course.id" :data-lid="typeof slot.lesson === 'undefined' ? 0 : slot.lesson.id" :data-eligible="slot.eligible" v-on:click="handleLessonClick">
                                    <p :class="$style.day">{{ getDayName(slot.day) }}</p>
                                    <p :class="$style.hour">{{ slot.hour + ':00 - ' + (slot.hour + 1) + ':00' }}</p>
                                </li>
                            </ul>
                        </li>
                    </ul>
                    <p :class="$style.instructions" v-if="!isUserAdmin">Click on any available slot to book a lesson, click on red marked lessons to cancel your reservation.</p>
                    <p :class="$style.instructions" v-if="isUserAdmin">Click on any reserved slot to cancel that reservation, on any available slot to book a lesson.</p>
                </div>
            </li>
        </ul>
        <p :class="$style.emptyMessage" v-else>No available lesson found</p>
    </div>
</template>
<script src="./available-lesson-list.js"></script>
<style lang="sass" src="./available-lesson-list.scss" module></style>
